package ui.config;

import java.io.File;
import java.util.*;

public class ConfigParser {


    private String configLocation;
    private HashMap<String,String> argsMap = new HashMap<>();

    public ConfigParser(String configLocation){
        makeMap(configLocation);
    }

    public ConfigParser(String[] args){
        makeMap(args);
    }

    public String getArg(String arg){
        if(containsKey(arg))
            return argsMap.get(arg);
        return null;
    }

    private void makeMap(String[] args){
        argsMap = new HashMap<>();

        for(int i = 0; i < args.length - 1; i++){
            String k = args[i];
            String v = args[i+1];
            argsMap.put(k,v);
        }
    }

    public boolean containsKey(String key){
        return argsMap.containsKey(key) && !isNullEquivalent(argsMap.get(key));
    }

    public void makeMap(String configLocation){
        try{
            Scanner config = new Scanner(new File(configLocation));
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

                argsMap.put(key,value);
            }
            config.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Iterator<String> iterator(){
        return argsMap.keySet().iterator();
    }

    public boolean isNullEquivalent(String s){
        return s.equalsIgnoreCase("")
                || s.equalsIgnoreCase("default");
    }
    private boolean skip(String k, String v) {
        return k.equalsIgnoreCase("--config");
    }

}
