package ui.tools.graphics;

import enums.Axis;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageTransform {
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static BufferedImage resize(BufferedImage img, int s, Axis axis){
        int w = img.getWidth();
        int h = img.getHeight();
        double scale;
        if(axis == Axis.HORIZONTAL){
            scale = ((double)h / (double)s);
        }else {
            scale = ((double)w / (double)s);
        }

        w = (int)((double)w * scale);
        h = (int)((double)h * scale);
        return resize(img,w,h);
    }
}
