package run;


import organization.ScreenOrganizer;
import raspberryPi.RPiInterface;
import realTime.ImmediateAction;
import realTime.TimeUnit;
import thirdparty.location.LocationApi;
import ui.config.Configuration;
import ui.config.Setup;
import ui.view.PiFrame;
import ui.view.PiScreen;
import ui.widget.*;

import java.awt.*;

public class Startup {
    //Host Machine Dependency [sudo [-H]] pip[2|3] install gps3
    //      https://pypi.org/project/gps3/

    public static void main(String[] args){
        Setup.setup(args);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = 1;
        gbc.insets = Configuration.GRIDBAG_INSETS;

        PiFrame pf = new PiFrame();

        PiScreen clockScreen = pf.createScreen(pf.getContentPane().getWidth(),pf.getContentPane().getHeight());

        InternetStatusPanel isp = new InternetStatusPanel();
        isp.setRefreshInterval(10,TimeUnit.MINUTES);
        clockScreen.getHiddenPanels().add(isp);
        ImmediateAction goodAction = new ImmediateAction() {
            @Override
            public void action() {
                clockScreen.removeFooterPanel(isp);
                RPiInterface.getLocalIpAddress();
            }
        };
        ImmediateAction badAction = new ImmediateAction() {
            @Override
            public void action() {
                clockScreen.addFooterPanel(isp);
            }
        };
        ImmediateAction extendedAction = new ImmediateAction() {
            @Override
            public void action() {
                if(!isp.getInternetStatus()){
                    RPiInterface.wakeScreen();
                }
            }
        };
        isp.setStatusTurnedGoodAction(goodAction);
        isp.setStatusTurnedBadAction(badAction);
        isp.setExtendedAction(extendedAction);

        Clock clock = new Clock(clockScreen.getW(),clockScreen.getH());
        clockScreen.addPiPanel(clock);

        ScreenOrganizer organizer = new ScreenOrganizer();
        organizer.setMainPanel(clock);
        clockScreen.setOrganizer(organizer);

        if(getLocationData()){
            TextPanel city = new TextPanel(Configuration.LOCATION_DATA.getCity()+", "+
                    Configuration.LOCATION_DATA.getRegion()+" "+
                    Configuration.LOCATION_DATA.getCountryName(),120,20);
            clockScreen.addFooterPanel(city);
        }


        WeatherImage weatherImage = new WeatherImage(150,60);
        weatherImage.updateAfter(1, TimeUnit.MINUTES);
        clockScreen.addPiPanel(weatherImage);

        LocalIpDisplay localIP = new LocalIpDisplay(60,30);
        localIP.setFormat("(%s)");
        clockScreen.addFooterPanel(localIP);
        localIP.setRefreshInterval(12,TimeUnit.HOURS);

        pf.timer(200);

        pf.pack();
        EventQueue.invokeLater(() -> {
            pf.setVisible(true);
        });
    }

    private static boolean getLocationData(){
        LocationApi client = new LocationApi();
        return client.configureLocation();
    }
}
