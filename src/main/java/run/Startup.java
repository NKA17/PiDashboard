package run;


import config.ConfigParser;
import organization.ScreenOrganizer;
import raspberryPi.Printer;
import raspberryPi.RPiInterface;
import realTime.ImmediateAction;
import realTime.TimeUnit;
import thirdparty.discord.DiscordHandler;
import thirdparty.location.LocationApi;
import config.Configuration;
import config.Setup;
import ui.view.PiFrame;
import ui.view.PiScreen;
import ui.widget.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Startup {
    //Host Machine Dependency [sudo [-H]] pip[2|3] install gps3
    //      https://pypi.org/project/gps3/

    public static void main(String[] args){
        Setup.setup(args);
        if(Configuration.MODE.equalsIgnoreCase("exec")){
            testExecMode();
        }

        Printer.println("---------------------------------------");
        Printer.println("Starting Dashboard...");
        Printer.println("---------------------------------------");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = 1;
        gbc.insets = Configuration.GRIDBAG_INSETS;

        PiFrame pf = new PiFrame();

        PiScreen clockScreen = pf.createScreen(pf.getContentPane().getWidth(),pf.getContentPane().getHeight());

        InternetStatusPanel isp = new InternetStatusPanel();
        isp.setRefreshInterval(10,TimeUnit.MINUTES);
        clockScreen.addHiddenPanel(isp);
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
                clockScreen.addFooterPanel(isp, 0);
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
        weatherImage.updateAfter(10, TimeUnit.MINUTES);
        clockScreen.addPiPanel(weatherImage);

        LocalIpDisplay localIP = new LocalIpDisplay(60,30);
        localIP.setFormat("(%s)");
        clockScreen.addFooterPanel(localIP);
        localIP.setRefreshInterval(12,TimeUnit.HOURS);

        pf.timer(100);

        if(!Configuration.DISPLAY_SHOW_CURSOR) {
            BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(0, 0), "blank cursor");
            pf.getContentPane().setCursor(blankCursor);
        }

        PiTempPanel ptp = new PiTempPanel();
        ptp.setRefreshInterval(2,TimeUnit.SECONDS);
        clockScreen.addPiPanel(ptp);

        DiscordHandler dh = new DiscordHandler();
        dh.start();

        DiscordMessagePiPanel dmp = new DiscordMessagePiPanel();
        dh.setObserved(dmp);
        clockScreen.addHiddenPanel(dmp);

        pf.pack();
        EventQueue.invokeLater(() -> {
            pf.setVisible(true);
        });
    }

    private static boolean getLocationData(){
        LocationApi client = new LocationApi();
        return client.configureLocation();
    }

    private static void testExecMode(){
        try{
            System.out.println("$ "+Setup.configParser.getArg("--message"));
            Process p = Runtime.getRuntime().exec(Setup.configParser.getArg("--message"));
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            int i = 100;
            String out;
            while((out = reader.readLine()) != null && (i--) > 0){
                System.out.println(out);
            }

            if(p.isAlive()){
                p.destroy();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.exit(-1);
    }
}
