package graphics;

import enums.Axis;
import ui.config.Configuration;
import ui.tools.graphics.ImageTransform;
import ui.widget.Spacer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

public class ImageCache {
    private static HashMap<String,BufferedImage> cache = new HashMap<>();

    public static BufferedImage get(String path, int s, Axis axis){
        String path2 = Configuration.IMAGE_LOCATION + path;

        BufferedImage image = get(path);
        int w = image.getWidth();
        int h = image.getHeight();
        double scale;
        if(axis == Axis.HORIZONTAL){
            scale = ((double)h / (double)s);
        }else {
            scale = ((double)w / (double)s);
        }

        w = (int)((double)w * scale);
        h = (int)((double)h * scale);
        if(image.getWidth() != w || image.getHeight() != h){
            image = ImageTransform.resize(image,w, h);
            cache.put(path2,image);
        }

        return image;
    }

    public static BufferedImage get(String path, int w, int h){
        String path2 = Configuration.IMAGE_LOCATION + path;

        BufferedImage image = get(path);
        if(image.getWidth() != w || image.getHeight() != h){
            image = ImageTransform.resize(image,w,h);
            cache.put(path2,image);
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
}
