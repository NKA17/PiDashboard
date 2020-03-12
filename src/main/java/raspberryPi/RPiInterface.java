package raspberryPi;

import realTime.DelayedAction;
import realTime.DoubleAction;
import config.Configuration;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;
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
    private static final String get_temperature = "vcgencmd measure_temp"; //vcgencmd measure_temp -> temp=50.8'C

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
            Printer.println("Getting local ip...");
            if(reader == null){
                return null;
            }
            Pattern p = Pattern.compile("(192.\\d+.\\d+.\\d+)");
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = p.matcher(line);
                if(m.find()){
                    Configuration.LOCATION_LOCAL_IP = m.toMatchResult().group(1);
                    Printer.println("Local IP = (%s)",Configuration.LOCATION_LOCAL_IP);
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
                boolean b = windowsCheckInternet();
                Printer.println("Internet connection = {%s}",(b)?("GOOD"):("BAD"));
                return b;
            }

            Process process = getProcess(check_internet);
            Printer.println("Checking internet connection...");

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

                    Printer.println("Internet connection = {%s}",(bytes > 0)?("GOOD"):("BAD"));
                    return bytes > 0;
                }
            }
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Printer.println("Internet connection = {%s}","BAD");
        return false;
    }

    private static boolean windowsCheckInternet(){
        try{
            Process process = Runtime.getRuntime().exec("ping google.com");
            Printer.println("Checking internet connection...");

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

    private static double tempMark = 0;
    public static double getTemperature(){
        try{
            if (windows()) {
                Random r = new Random();
                return r.nextDouble()*30 + 30;
            }

            Process process = Runtime.getRuntime().exec(get_temperature);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Pattern p = Pattern.compile("temp=(\\d+\\.\\d+)");
            String line;
            while((line = reader.readLine()) != null){
                Matcher m = p.matcher(line);
                if(m.find()){
                    double temp = Double.parseDouble(m.group(1));

                    double tempDX = Math.abs(tempMark - temp);
                    if(tempDX >=5) {
                        tempMark = temp;
                        Printer.println("Temperature = %f", temp);
                    }
                    return temp;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
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
            Printer.println("Getting public ip...");
            if(reader == null){
                return null;
            }
            Pattern p = Pattern.compile("(\\d+.\\d+.\\d+.\\d+)");
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = p.matcher(line);
                if(m.find()){
                    Configuration.LOCATION_PUBLIC_IP = m.toMatchResult().group(1);
                    Printer.println("Public IP = (%s)",Configuration.LOCATION_PUBLIC_IP);
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
            Process process = runScript(scriptName);

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
            Process process = runScript(scriptName);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            return reader;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static Process runScript(String scriptName){
        try{
            if(Configuration.SCRIPT_LOCATION == null){
                return null;
            }
            String scriptPath = String.format("./%s/%s", Configuration.SCRIPT_LOCATION,scriptName).replaceAll("//","/");
            Printer.println("Running script {%s}",scriptPath);
            return Runtime.getRuntime().exec(scriptPath);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static Process runScript(String scriptName, String...args){
        try{
            if(Configuration.SCRIPT_LOCATION == null){
                return null;
            }
            String argString = "";
            for(String a: args){
                argString += a+" ";
            }
            return Runtime.getRuntime().exec(String.format("./%s/%s ", Configuration.SCRIPT_LOCATION,scriptName).replaceAll("//","/")+argString);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //Process statuses
    //https://www.liquidweb.com/kb/linux-process-statuses/

    //show thread $ ps H

    private static boolean windows(){
        return System.getProperty("os.name").contains("Windows");
    }
//
    public static void main(String[] args){
        try{
            Scanner in = new Scanner(System.in);
            String input;
            while(true){
                System.out.print("$ ");
                input = in.nextLine().trim();
                if(input.equalsIgnoreCase("quit()")){
                    break;
                }else {
                    Process p = Runtime.getRuntime().exec(input);
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

                    System.out.println();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void emergencyTerminate(long delayMillis, String reason){
        Printer.println("Emergency exit of dashboard in %d, message: %s",delayMillis,reason);

        DelayedAction da = new DelayedAction(delayMillis) {
            @Override
            public void action() {
                System.exit(-1);
            }
        };
        da.setDescription("Emergency exit event");
        da.deploy();

    }
    public static void emergencyTerminate(String reason){
        emergencyTerminate(0,reason);
    }
}
