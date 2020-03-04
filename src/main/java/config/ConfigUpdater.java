package config;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class ConfigUpdater {
    public static void main(String[] args) {
        //Configuration.SCRIPT_LOCATION = "scripts";
        //RPiInterface.checkInternetConnection();

        ConfigParser local = new ConfigParser(args[0]);
        ConfigParser remote = new ConfigParser(args[1]);

        Iterator<String> localIter = local.iterator();
        Iterator<String> remoteIter = remote.iterator();

        List<String> kv = new ArrayList<>();

        while (remoteIter.hasNext()) {
            String k = remoteIter.next();
            if (local.containsKey(k)) {
                kv.add(String.format("%s=%s\n", k, local.getArg(k)));
            } else {
                kv.add(String.format("%s=\n", k));
            }
        }
        while(localIter.hasNext()){
            String k = localIter.next();
            if(local.containsKey(k)){
                String s = String.format("%s=%s\n",k,local.getArg(k));
                if(!kv.contains(s)){
                    kv.add("(deprecated) "+s.replaceAll("\\(deprecated\\)\\s*",""));
                }
            }
        }

        try {
            Collections.sort(kv, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });
            System.out.println("\nnew config:\n");
            PrintWriter pw = new PrintWriter(new File(args[0]));
            for(String s: kv){
                System.out.print(s);
                pw.print(s);
                pw.flush();
            }
            pw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
