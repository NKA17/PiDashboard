package ui.tools.graphics;

import java.awt.*;

public class GraphicsStore {
    private Color c = null;
    private Font f = null;

    public void stash(Graphics g){
        c = g.getColor();
        f = g.getFont();
    }

    public void restore(Graphics g){
        g.setColor(c);
        g.setFont(f);
    }
}
