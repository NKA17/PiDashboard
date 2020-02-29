package ui.config;

import java.awt.*;
import java.util.HashMap;

public class Setup {

    private static ConfigParser configParser = null;
    public static void setup(String[] args){
        System.out.println("Setup...");
        String c = getArg("--config",args);

        configParser = c != null
                ? new ConfigParser(c)
                : new ConfigParser(args);

        scripts(args);
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
    }

    private static void sleepAfter(){
        String m = getArg("--sleepAfter");
        if(m != null) {
            Configuration.SLEEP_AFTER_TIME = Integer.parseInt(m);
        }

    }

    private static void print(){
        String p = getArg("--print");
        if(p != null){
            Configuration.PRINT = Boolean.parseBoolean(p);
        }

        System.out.println("\tPrint Statements: "+p);
    }

    private static void wakeScreenTime(){
        String m = getArg("--wake");
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

    private static void cycleTime(String[] args){
        String m = getArg("--cycleTime",args);
        if(m != null) {
            Configuration.SWAP_TIME = Integer.parseInt(m);
        }

        System.out.println("\tShow updated panels for "+(Configuration.SWAP_TIME/1000)+" seconds.");
    }

    private static void scripts(String[] args){
        String scripts = getArg("--scripts",args);
        Configuration.SCRIPT_LOCATION = scripts;

        System.out.println("\tScripts Location: "+Configuration.SCRIPT_LOCATION);
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

        String width = getArg("--width",args);
        String height = getArg("--height",args);

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
        if(hasArg("--window_color")){
            Configuration.WINDOW_BG_COLOR = getColor("--window_color");
        }
    }

    private static void panelColor(){
        if(hasArg("--panel_color")){
            Configuration.PANEL_BG_COLOR = getColor("--panel_color");
        }
    }

    private static void screenColor(){
        if(hasArg("--screen_color")){
            Configuration.SCREEN_BG_COLOR = getColor("--screen_color");
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
