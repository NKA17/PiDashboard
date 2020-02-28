package raspberryPi;

import ui.config.ConfigParser;
import ui.config.Configuration;

public class Printer {
    public static void println(Object o){
        print(o);
        print("\n");
    }

    public static void println(Object o, Object...o2){
        String s = o.toString();
        for(Object obj: o2){
            s += obj.toString();
        }
        println(s);
    }

    public static void print(Object o){
        if(Configuration.PRINT){
            System.out.print(o);
        }
    }
}
