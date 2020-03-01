package ui.widget;

import ui.config.Configuration;
import ui.tools.font.FontInfo;
import ui.tools.font.FontTool;
import ui.view.PiPanel;

import java.awt.*;

public class TextPanel extends PiPanel {

    private String text = "";

    public TextPanel() {
        super();
        setForeground(Configuration.TEXT_COLOR);
    }

    public TextPanel(int w, int h) {
        super(w, h);
        setForeground(Configuration.TEXT_COLOR);
    }

    public TextPanel(String text) {
        this();
        this.text = text;
    }

    public TextPanel(String text, int w, int h) {
        this(w, h);
        this.text = text;
    }

    @Override
    public void draw(Graphics g) {
        int L = Configuration.GRIDBAG_INSETS.left;
        int R = Configuration.GRIDBAG_INSETS.right;
        int T = Configuration.GRIDBAG_INSETS.top;
        int B = Configuration.GRIDBAG_INSETS.bottom;

        FontInfo fontInfo = FontTool.fitInBox(Configuration.TEXT_FONT,getText(),getW()-L-R,getH()-T-B);
        g.setColor(getForeground());
        g.setFont(fontInfo.getFont());
        int centerOffset = (getH()-fontInfo.getH()) / 2;
        FontTool.drawAAString(g,getText(),getX()+L,getY()+centerOffset);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isPowered() {
        return false;
    }
}
