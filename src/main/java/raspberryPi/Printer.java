package raspberryPi;

import config.Configuration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Printer {

    public static void println(String format, Object...objects){
        println(String.format(format,objects));
    }

    public static void println(Object o){
        print(o.toString()+"\n");
    }

    public static void println(Object o, Object...o2){
        String s = o.toString();
        for(Object obj: o2){
            s += obj.toString();
        }
        println(s);
    }

    public static void print(String s){
        if(Configuration.PRINT){
            System.out.print(s);
        }
        if(Configuration.LOGGING){
            log(s);
        }
    }

    public static void log(String log){
        try {
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat(Configuration.LOGGING_FILE_FORMAT);
            String fileName = "log_"+sdf.format(ts);

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(Configuration.LOGGING_PATH+fileName+".txt", true)  //Set true for append mode
            );

            SimpleDateFormat timeStamp = new SimpleDateFormat("[MM.dd.yyyy - HH:mm.ss.SSS]:\t");

            writer.write(timeStamp.format(ts)+log.replaceAll("\n",""));
            writer.newLine();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void log(String log, Object...objects){
        log(String.format(log,objects));
    }
}
