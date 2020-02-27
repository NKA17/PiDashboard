package run;

import thirdparty.weather.WeatherAPI;
import thirdparty.weather.WeatherData;
import ui.config.Configuration;
import ui.config.Setup;
import ui.view.PiFrame;
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

        Clock clock = new Clock(pf.getScreenPanel().getW(),pf.getScreenPanel().getH());
        clock.setUseMeridean(false);
        pf.addPiPanel(clock);

//        WeatherAPI weatherAPI = new WeatherAPI();
//        WeatherData weather = weatherAPI.getWeatherNow();
//        WeatherImage weatherImage = new WeatherImage(weather);
//        pf.addPiPanel(weatherImage);

        pf.timer(200);

        pf.pack();
        EventQueue.invokeLater(() -> {
            pf.setVisible(true);
        });
    }


}
