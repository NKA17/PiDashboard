package config;

import java.io.File;
import java.util.*;

public class ConfigParser {


    private String configLocation;
    private boolean ignoreCommentsInValue =  true;
    private HashMap<String,String> argsMap = new HashMap<>();

    public ConfigParser(String configLocation){
        this(configLocation,true);
    }

    public ConfigParser(String configLocation, boolean ignoreCommentsInValue){
        this.ignoreCommentsInValue = ignoreCommentsInValue;
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
                if(line.startsWith("#")){
                    continue;
                }

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
        return k.equalsIgnoreCase("--config")
                || (ignoreCommentsInValue && v.startsWith("#"));
    }

    public boolean getBool(String key){
        if(containsKey(key)){
            return getArg(key).equalsIgnoreCase("true");
        }else {
            return false;
        }
    }

    public int getInt(String key){
        if(containsKey(key)){
            return Integer.parseInt(getArg(key));
        }else {
            return 0;
        }
    }

    public long getLong(String key){
        if(containsKey(key)){
            return Long.parseLong(getArg(key));
        }else {
            return 0;
        }
    }

    public boolean getBool(String key, boolean defaultValue){
        if(containsKey(key)){
            return getArg(key).equalsIgnoreCase("true");
        }else {
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue){
        if(containsKey(key)){
            return Integer.parseInt(getArg(key));
        }else {
            return defaultValue;
        }
    }

    public long getLong(String key, long defaultValue){
        if(containsKey(key)){
            return Long.parseLong(getArg(key));
        }else {
            return defaultValue;
        }
    }

    public String getArg(String arg, String defaultValue){
        if(containsKey(arg))
            return argsMap.get(arg);
        return defaultValue;
    }
}
