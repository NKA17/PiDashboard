package graphics;

import enums.Axis;
import config.Configuration;
import ui.tools.graphics.ImageTransform;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

public class ImageCache {
    private static HashMap<String,BufferedImage> cache = new HashMap<>();

    public static BufferedImage get(String path, int s, Axis axis){
        String path2 = Configuration.IMAGE_LOCATION + path;

        try {
            BufferedImage image = ImageIO.read(new File(path));
            int w = image.getWidth();
            int h = image.getHeight();
            double scale;
            if (axis == Axis.HORIZONTAL) {
                scale = ((double) h / (double) s);
            } else {
                scale = ((double) w / (double) s);
            }

            w = (int) ((double) w * scale);
            h = (int) ((double) h * scale);
            if (image.getWidth() != w || image.getHeight() != h) {
                image = ImageTransform.resize(image, w, h);
                cache.put(path2, image);
            }

            return image;
        }catch (Exception e){
            return get(path);
        }
    }

    public static boolean checkFor(String path){

        String path2 = Configuration.IMAGE_LOCATION + path;
        File file = new File(path2);
        return file.exists();
    }

    public static boolean contains(String key){
        return cache.containsKey(key);
    }

    public static BufferedImage get(String path, int w, int h){
        String path2 = Configuration.IMAGE_LOCATION + path;

        BufferedImage image = get(path);
        if(image.getWidth() != w || image.getHeight() != h){
            try {
                image = ImageIO.read(new File(path2));
                image = ImageTransform.resize(image, w, h);
                cache.put(path2, image);
            }catch (Exception e){ e.printStackTrace();}
        }

        return image;
    }

    public static BufferedImage get(String path){
        path = Configuration.IMAGE_LOCATION + path;

        if(cache.containsKey(path)){
            return cache.get(path);
        }else{
            try {
                BufferedImage image = ImageIO.read(new File(path));
                cache.put(path,image);
                return image;
            }catch (Exception e){
                BufferedImage image = new BufferedImage(50,50,BufferedImage.TYPE_4BYTE_ABGR);
                image.getGraphics().setColor(new Color(30,30,30));
                image.getGraphics().fillRect(0,0,50,50);
                return image;
            }
        }
    }

    public static void put(String key, BufferedImage image){
        cache.put(key,image);
    }
}
