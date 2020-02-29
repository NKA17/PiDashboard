package raspberryPi;

import javafx.application.ConditionalFeature;
import realTime.DoubleAction;
import ui.config.Configuration;

public class RPiInterface {
    /**
     * Turn on tv https://timleland.com/raspberry-pi-turn-tv-onoff-cec/
     * Turn tv on: echo on 0 | cec-client -s -d 1
       Turn tv off: echo standby 0 | cec-client -s -d 1
       Tv status: echo pow 0 | cec-client -s -d 1
     * @param time
     */

    private static final String wake_screen = "wake_screen.sh";
    private static final String sleep_screen = "sleep_screen.sh";

    public static void wakeScreenTemp(int time){
        //vcgencmd display_power 0
        DoubleAction wakeScreenAction = new DoubleAction(time) {
            @Override
            public void open() {
                wakeScreen();
            }

            @Override
            public void close() {
                sleepScreen();
            }
        };

        wakeScreenAction.deploy();
    }

    public static void wakeScreen(){
        //Configuration.SCREEN_BG_COLOR = Configuration.PANEL_BG_COLOR;
        runScript(wake_screen);
    }

    public static void sleepScreen(){
        //Configuration.SCREEN_BG_COLOR = Configuration.WINDOW_BG_COLOR;
        runScript(sleep_screen);
    }

    private static Process getProcess(String scriptName){
        try{
            if(Configuration.SCRIPT_LOCATION == null){
                return null;
            }
            return Runtime.getRuntime().exec(String.format("./%s/%s", Configuration.SCRIPT_LOCATION,scriptName).replaceAll("//","/"));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static void runScript(String scriptName){
        try{
            if(Configuration.SCRIPT_LOCATION == null){
                return;
            }
            Runtime.getRuntime().exec(String.format("./%s/%s", Configuration.SCRIPT_LOCATION,scriptName).replaceAll("//","/"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
