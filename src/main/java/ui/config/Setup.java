package ui.config;

public class Setup {

    public static void setup(String[] args){
        System.out.println();
        scripts(args);
        dimensions(args);
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
        for(int i = 0; i < args.length-1; i++){
            if(arg.equalsIgnoreCase(args[i]))
                return args[i+1];
        }

        return null;
    }

    private static boolean checkFlag(String flag, String[] args){
        for(int i = 0; i < args.length; i++){
            if(flag.equalsIgnoreCase(args[i]))
                return true;
        }

        return false;
    }
}
