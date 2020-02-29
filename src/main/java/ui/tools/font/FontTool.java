package ui.tools.font;

import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class FontTool {

    public static FontInfo fitInBox(Font font, String text, int boxW, int boxH){
        float fontSize = 20.0f;

        font = font.deriveFont(fontSize);
        Rectangle rect = getStringBounds(font, text);
        fontSize = (float) (boxW / rect.getWidth() ) * fontSize;

        font = font.deriveFont(fontSize);
        rect = getStringBounds(font,text);

        if(rect.getHeight() > boxH){
            fontSize = (float) (boxH / rect.getHeight()) * fontSize;
            font = font.deriveFont(fontSize);
            rect = getStringBounds(font,text);
        }

        FontInfo fontInfo = new FontInfo();
        fontInfo.setFont(font);
        fontInfo.setW(rect.width);
        fontInfo.setH(rect.height);

        return fontInfo;
    }


    public static FontInfo fitInBox2(Font f, String text, int boxW, int boxH, int leftMargin, int rightMargin, int topMargin, int bottomMargin){
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
            Rectangle2D rect = getStringBounds(font, text);
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
        Rectangle rect = getStringBounds(font,text);

        FontInfo fontInfo = new FontInfo();
        fontInfo.setFont(font);
        fontInfo.setW((int)rect.getWidth());
        fontInfo.setH((int)(rect.getHeight()));

        return fontInfo;
    }

    private static Rectangle getStringBounds(Font font, String str)
    {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        GlyphVector gv = font.createGlyphVector(frc, str);
        return gv.getPixelBounds(null, 0,0);
    }

    public static void drawAAString(Graphics g, String text, int x, int y){
        Rectangle rect = getStringBounds(g.getFont(),text);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawString(text,x,y+rect.height);
    }
}
