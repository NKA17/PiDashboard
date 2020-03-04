package config;

import java.awt.*;

public class Setup {

    private static ConfigParser configParser = null;
    public static void setup(String[] args){
        System.out.println("Setup...");
        String c = getArg("--config",args);

        configParser = c != null
                ? new ConfigParser(c)
                : new ConfigParser(args);

        scripts(args);
        images(args);
        dimensions(args);
        wakeScreenTime();
        sleepAfter();
        cycleTime(args);
        print();
        windowColor();
        panelColor();
        screenColor();
        clockUseMeridian();
        clockUseMilitary();
        checkInternet();
        ip();
        localIP();
        longitude();
        latitude();
        logging();
    }

    private static void logging(){
        String m = getArg("--out.logging");
        if(m != null){
            Configuration.LOGGING = m.equalsIgnoreCase("true");
        }

        String p;
        p = getArg("--out.logging.path");
        if(p != null){
            Configuration.LOGGING_PATH = p;
        }

        p = getArg("--out.logging.fileFormat");
        if(p != null){
            Configuration.LOGGING_FILE_FORMAT = p;
        }
    }

    private static void longitude(){
        String m = getArg("--location.longitude");
        if(m != null) {
            Configuration.LOCATION_LONGITUDE = m;
        }
    }
    private static void latitude(){
        String m = getArg("--location.latitude");
        if(m != null) {
            Configuration.LOCATION_LATITUDE = m;
        }
    }

    private static void ip(){
        String m = getArg("--location.ip");
        if(m != null) {
            Configuration.LOCATION_PUBLIC_IP = m;
        }
    }

    private static void localIP(){
        String m = getArg("--location.local_ip");
        if(m != null) {
            Configuration.LOCATION_LOCAL_IP = m;
        }
    }

    private static void sleepAfter(){
        String m = getArg("--time.sleepAfter");
        if(m != null) {
            Configuration.SLEEP_AFTER_TIME = Integer.parseInt(m);
        }

    }

    private static void print(){
        String p = getArg("--out.print");
        if(p != null){
            Configuration.PRINT = Boolean.parseBoolean(p);
        }

        System.out.println("\tPrint Statements: "+Configuration.PRINT);
    }

    private static void wakeScreenTime(){
        String m = getArg("--time.wake");
        if(m != null) {
            Configuration.WAKE = Boolean.parseBoolean(m);
        }
    }

    private static void clockUseMilitary(){
        String m = getArg("clock.military");
        if(m != null) {
            Configuration.CLOCK_MILITARY = Boolean.parseBoolean(m);
        }
    }

    private static void clockUseMeridian(){
        String m = getArg("clock.meridian");
        if(m != null) {
            Configuration.CLOCK_MERIDIAN = Boolean.parseBoolean(m);
        }
    }

    private static void checkInternet(){
        String m = getArg("--check.internet");
        if(m != null) {
            Configuration.CHECK_INTERNET = Boolean.parseBoolean(m);
        }
    }

    private static void cycleTime(String[] args){
        String m = getArg("--time.cycleTime",args);
        if(m != null) {
            Configuration.SWAP_TIME = Integer.parseInt(m);
        }

        System.out.println("\tShow updated panels for "+(Configuration.SWAP_TIME/1000)+" seconds.");
    }

    private static void scripts(String[] args){
        String scripts = getArg("--path.scripts",args);

        if(scripts != null) {
            Configuration.SCRIPT_LOCATION = scripts;
        }

        System.out.println("\tScripts Location: "+Configuration.SCRIPT_LOCATION);
    }

    private static void images(String[] args){
        String images = getArg("--path.images");

        if(images != null){
            Configuration.IMAGE_LOCATION = images;
        }


        System.out.println("\tImages Location: "+Configuration.IMAGE_LOCATION);
    }

    private static void dimensions(String[] args){
        int w = -1;
        int h = -1;

        String dimensions = getArg("--dim", args);

        if(dimensions!=null) {
            String[] dims = dimensions.split("x");
            w = Integer.parseInt(dims[0]);
            h = Integer.parseInt(dims[1]);
        }

        String width = getArg("--dim.width",args);
        String height = getArg("--dim.height",args);

        if(width!=null){
            w = Integer.parseInt(width);
        }
        if(height!=null){
            h = Integer.parseInt(height);
        }

        System.out.println(String.format(
                "\tConstraints given: width=%s, height=%s",
                ((w==-1)?"none":w+""),
                ((h==-1)?"none":h+"")
        ));

        if(w!=-1){
            Configuration.WIDTH_CONSTRAINT = w;
        }
        if(h!=-1){
            Configuration.HEIGHT_CONSTRAINT = h;
        }
    }

    private static String getArg(String arg, String[] args){
        if(configParser != null){
            return getArg(arg);
        }

        //this for loop should never be hit but its here just in case
        for(int i = 0; i < args.length-1; i++){
            if(arg.equalsIgnoreCase(args[i]))
                return args[i+1];
        }

        return null;
    }

    private static String getArg(String arg){
        return configParser.getArg(arg);
    }

    private static boolean checkFlag(String flag, String[] args){
        for(int i = 0; i < args.length; i++){
            if(flag.equalsIgnoreCase(args[i]))
                return true;
        }

        return false;
    }

    private static void windowColor(){
        if(hasArg("--color.window")){
            Configuration.WINDOW_BG_COLOR = getColor("--color.window");
        }
    }

    private static void panelColor(){
        if(hasArg("--color.panel")){
            Configuration.PANEL_BG_COLOR = getColor("--color.panel");
        }
    }

    private static void screenColor(){
        if(hasArg("--color.screen")){
            Configuration.SCREEN_BG_COLOR = getColor("--color.screen");
        }
    }

    private static Color getColor(String arg){
        String c = getArg(arg);
        if(c == null || c.equalsIgnoreCase("null")){
            return null;
        }
        String[] split = c.split(",");
        return new Color(
            Integer.parseInt(split[0]),
            Integer.parseInt(split[1]),
            Integer.parseInt(split[2])
        );
    }

    private static boolean hasArg(String arg){
        return configParser.containsKey(arg);
    }
}
