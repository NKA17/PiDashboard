package run;


import organization.ScreenOrganizer;
import realTime.TimeUnit;
import ui.config.Configuration;
import ui.config.Setup;
import ui.view.PiFrame;
import ui.view.PiScreen;
import ui.widget.Clock;
import ui.widget.WeatherImage;

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

        PiScreen clockScreen = pf.createScreen(pf.getContentPane().getWidth(),pf.getContentPane().getHeight()-20);
        Clock clock = new Clock(clockScreen.getW(),clockScreen.getH());
        clock.setUseMeridean(false);
        clockScreen.addPiPanel(clock);

        ScreenOrganizer organizer = new ScreenOrganizer();
        organizer.setMainPanel(clock);
        clockScreen.setOrganizer(organizer);

        Clock c = new Clock(100,60);
        c.setX(200);
        //clockScreen.addPiPanel(c);

        WeatherImage weatherImage = new WeatherImage(150,60);
        weatherImage.updateAfter(1, TimeUnit.MINUTES);
        clockScreen.addPiPanel(weatherImage);

        pf.timer(200);

        pf.pack();
        EventQueue.invokeLater(() -> {
            pf.setVisible(true);
        });
    }


}
