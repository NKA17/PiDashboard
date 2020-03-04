package ui.widget;

import raspberryPi.RPiInterface;
import config.Configuration;
import ui.tools.font.FontInfo;
import ui.tools.font.FontTool;
import ui.view.PiPanel;

import java.awt.*;

public class LocalIpDisplay extends PiPanel {

    private String format = "%s";

    public LocalIpDisplay(){
        this(100,30);
    }

    public LocalIpDisplay(int w, int h){
        super(w,h);
    }

    @Override
    public void draw(Graphics g) {
        int L = Configuration.GRIDBAG_INSETS.left;
        int R = Configuration.GRIDBAG_INSETS.right;
        int T = Configuration.GRIDBAG_INSETS.top;
        int B = Configuration.GRIDBAG_INSETS.bottom;

        String text = Configuration.LOCATION_LOCAL_IP == null
                ? "Local IP: ?"
                : String.format(format,Configuration.LOCATION_LOCAL_IP);


        FontInfo fontInfo = FontTool.fitInBox(Configuration.TEXT_FONT,text,getW()-L-R,getH()-T-B);
        g.setColor(Configuration.TEXT_COLOR);
        g.setFont(fontInfo.getFont());
        int centerOffset = (getH()/2 - fontInfo.getH()/2);
        FontTool.drawAAString(g,text,getX()+L,getY()+centerOffset+T/2);
    }

    @Override
    public void update() {
        RPiInterface.getLocalIpAddress();
    }

    @Override
    public boolean isPowered() {
        return true;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
