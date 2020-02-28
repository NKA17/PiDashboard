package ui.config;

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
        wakeScreenTime(args);
        cycleTime(args);
        print();
    }

    private static void print(){
        String p = getArg("--print");
        if(p != null){
            Configuration.PRINT = Boolean.parseBoolean(p);
        }

        System.out.println("\tPrint Statements: "+p);
    }

    private static void wakeScreenTime(String[] args){
        String m = getArg("--wakeTime",args);
        if(m != null) {
            Configuration.WAKE_SCREEN_TIME = Integer.parseInt(m);
        }

        System.out.println("\tWake screen for "+(Configuration.WAKE_SCREEN_TIME/1000)+" seconds on updates.");
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
}
