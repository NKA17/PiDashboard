package raspberryPi;

import javafx.application.ConditionalFeature;
import realTime.DoubleAction;
import ui.config.Configuration;
import ui.config.Setup;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final String get_public_ip_address = "get_public_ip.sh";
    private static final String get_local_ip_address = "get_local_ip.sh";
    private static final String check_internet = "check_internet.sh";
    private static final String kill_process = "kill_process.sh";

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
        if(windows()){
            Configuration.SCREEN_BG_COLOR = Configuration.PANEL_BG_COLOR;
        }else {
            runScript(wake_screen);
        }
    }

    public static void sleepScreen(){
        if(windows()) {
            Configuration.SCREEN_BG_COLOR = Configuration.WINDOW_BG_COLOR;
        }else {
            runScript(sleep_screen);
        }
    }


    private static String getLocalIpWindows(){
        return "192.168.0.10";
    }

    public static String getLocalIpAddress(){
        try {
            if(windows()){
                return getLocalIpWindows();
            }

            BufferedReader reader = getProcessInput(get_local_ip_address);
            if(reader == null){
                return null;
            }
            Pattern p = Pattern.compile("(192.\\d+.\\d+.\\d+)");
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = p.matcher(line);
                if(m.find()){
                    Configuration.LOCATION_LOCAL_IP = m.toMatchResult().group(1);
                    return m.toMatchResult().group(1);
                }
            }
            reader.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkInternetConnection(){
        try {
            if(windows()){
                return windowsCheckInternet();
            }

            Process process = getProcess(check_internet);

            if(process == null){
                return false;
            }
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            if(reader == null){
                return false;
            }
            Pattern p = Pattern.compile("(\\d+) bytes from");
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = p.matcher(line);
                if(m.find()){
                    int bytes = Integer.parseInt(m.toMatchResult().group(1));
                    Configuration.CONNTECTED_TO_INTERNET = bytes > 0;
                    return bytes > 0;
                }
            }
            reader.close();
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private static boolean windowsCheckInternet(){
        try{
            Process process = Runtime.getRuntime().exec("ping google.com");

            if(process == null){
                return false;
            }
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            if(reader == null){
                return false;
            }
            Pattern good = Pattern.compile("((?i)Reply From)|(Request timed out.)");
            Pattern bad = Pattern.compile("(?i)request could not find host");
            String line;
            while ((line = reader.readLine()) != null) {
                if(good.matcher(line).find())
                    return true;
                if(bad.matcher(line).find())
                    return false;
            }
            reader.close();
            process.destroy();
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static String windowsGetPublicIpAddress(){
        try{
            //curl http://myexternalip.com/raw
            Process process = Runtime.getRuntime().exec("nslookup myip.opendns.com. resolver1.opendns.com");
            return "166.181.86.119";
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static String getPublicIpAddress(){
        try {
            BufferedReader reader = getProcessInput(get_public_ip_address);
            if(reader == null){
                return null;
            }
            Pattern p = Pattern.compile("(\\d+.\\d+.\\d+.\\d+)");
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = p.matcher(line);
                if(m.find()){
                    Configuration.LOCATION_PUBLIC_IP = m.toMatchResult().group(1);
                    return m.toMatchResult().group(1);
                }
            }
            reader.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Process getProcess(String scriptName){
        try{
            if(Configuration.SCRIPT_LOCATION == null){
                return null;
            }
            Process process = Runtime.getRuntime().exec(String.format("./%s/%s", Configuration.SCRIPT_LOCATION,scriptName).replaceAll("//","/"));

            return process;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedReader getProcessInput(String scriptName){
        try{
            if(Configuration.SCRIPT_LOCATION.equalsIgnoreCase("null")){
                return null;
            }
            Process process = Runtime.getRuntime().exec(String.format("./%s/%s", Configuration.SCRIPT_LOCATION,scriptName).replaceAll("//","/"));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            return reader;
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

    private static void runScript(String scriptName, String...args){
        try{
            if(Configuration.SCRIPT_LOCATION == null){
                return;
            }
            String argString = "";
            for(String a: args){
                argString += a+" ";
            }
            Runtime.getRuntime().exec(String.format("./%s/%s ", Configuration.SCRIPT_LOCATION,scriptName).replaceAll("//","/")+argString);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void kill(Process p){
        runScript(kill_process,p.toString());
    }

    //Process statuses
    //https://www.liquidweb.com/kb/linux-process-statuses/

    //show thread $ ps H

    private static boolean windows(){
        return System.getProperty("os.name").contains("Windows");
    }
//
    public static void main(String[] args){
        checkInternetConnection();
    }
}
