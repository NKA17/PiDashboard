package ui.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConfigParser {


    private String configLocation;

    public ConfigParser(String configLocation){
        this.configLocation = configLocation;
    }

    public void apply(){
        try{
            Scanner config = new Scanner(new File(configLocation));
            List<String> args = new ArrayList<>();
            while (config.hasNextLine()){
                String line = config.nextLine();
                int index = line.indexOf('=');
                if(index == -1)
                    continue;

                String key = line.substring(0,index).trim();
                String value = line.substring(index+1).trim();

                if(skip(key,value)){
                    continue;
                }

                args.add(key);
                args.add(value);
            }
            String[] arrayArgs = new String[args.size()];
            for(int i = 0; i < arrayArgs.length; i++){
                arrayArgs[i] = args.get(i);
            }
            Setup.setup(arrayArgs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean skip(String k, String v) {
        return k.equalsIgnoreCase("--config")
                || v.equalsIgnoreCase("")
                || v.equalsIgnoreCase("default");
    }

}
