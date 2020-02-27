package ui.tools.font;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class FontTool {

    public static FontInfo fitInBox(Font f, String text, int boxW, int boxH){
        return fitInBox(f,text,boxW,boxH,0,0);
    }

    public static FontInfo fitInBox(Font f, String text, int boxW, int boxH, int xMargin, int yMargin){
        return fitInBox(f,text,boxW,boxH,xMargin,xMargin,yMargin,yMargin);
    }

    public static FontInfo fitInBox(Font f, String text, int boxW, int boxH, int leftMargin, int rightMargin, int topMargin, int bottomMargin){
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,false,false);

        int w = boxW - leftMargin - rightMargin;
        int h = boxH - topMargin - bottomMargin;
        int fSize = 0;

        int uw = 1;
        int uh = 1;

        Font font;
        while(true) {
            fSize++;
            font = new Font(f.getFontName(),f.getStyle(),fSize);
            Rectangle2D rect = font.getStringBounds(text, frc);
            if(!((rect.getWidth() <= w || w <= 0) && (rect.getHeight() <= h || h <= 0))) {
                fSize--;
                font = new Font(f.getFontName(),f.getStyle(),fSize);
                rect = font.getStringBounds(text, frc);
                uw = (int) (rect.getWidth());
                uh = (int) rect.getHeight();
                break;
            }
        }

        FontInfo info = new FontInfo();
        info.setW(uw);
        info.setH(uh);
        info.setFont(new Font(f.getFontName(),f.getStyle(),fSize));
        return info;
    }

    public static FontInfo getBounds(Font font, String text){
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        Rectangle2D rect = font.getStringBounds(text, frc);

        FontInfo fontInfo = new FontInfo();
        fontInfo.setFont(font);
        fontInfo.setW((int)rect.getWidth());
        fontInfo.setH((int)rect.getHeight());

        return fontInfo;
    }
}
