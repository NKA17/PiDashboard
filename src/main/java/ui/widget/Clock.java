package ui.widget;

import raspberryPi.RPiInterface;
import realTime.DelayedAction;
import ui.config.Configuration;
import ui.tools.font.FontInfo;
import ui.tools.font.FontTool;
import ui.tools.graphics.GraphicsStore;
import ui.view.PiPanel;

import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Clock extends PiPanel {
    private FontInfo fontInfo;
    private Font font;
    private String format = "H:mm";
    private boolean useMeridean = true;

    public void useRegularPersonTime(){
        format = format.split(" ")[0]+ (useMeridean ? " xm" : "");
        configureFont(false);
    }

    public void useMilitaryTime(){
        format = "HHmm";
        configureFont(false);
    }

    public Clock(int w, int h){
        super(w,h);
        setW(w);
        setH(h);
        font = Configuration.TEXT_FONT;
        useRegularPersonTime();
        setForeground(Configuration.TEXT_COLOR);
        setRefreshInterval(1000);
    }

    private void configureFont(boolean inset){
        Insets insets = Configuration.GRIDBAG_INSETS;
        int l = inset ? insets.left : 0;
        int r = inset ? insets.right : 0;
        int t = inset ? insets.top : 0;
        int b = inset ? insets.bottom : 0;

        fontInfo = FontTool.fitInBox(font,"0"+format,getW(),getH(),l,r,t,b);
        font = fontInfo.getFont();

        //setW(fontInfo.getW() + (inset ? (l+r) : 0));
        //setH(fontInfo.getH() + (inset ? (t+b) : 0));
    }

    public int getFontHeight(){
        return FontTool.getBounds(font,format).getH();
    }
    @Override
    public void draw(Graphics g) {
        GraphicsStore gs = new GraphicsStore();
        gs.stash(g);

        String time = getTime();

        int ux = fontInfo.getW() - FontTool.getBounds(font,format).getW();
        FontInfo ff = FontTool.getBounds(font,time);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.setFont(font);
        g2.drawString(time,ux + getX(),getY()+ff.getH());

//        g.setColor(Color.RED);
//        g.drawRect(getX(),getY(),getW(),getH());
//        g.setColor(Color.YELLOW);
//        g.drawRect(getX(),getY(),ff.getW(),ff.getH());

        gs.restore(g);
    }

    public String getTime(){
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(format.replaceAll(" xm",""));
        String time = sdf.format(ts);

        if(format.equalsIgnoreCase("HHmm")){
            return time;
        }

        String[] split = time.split(":");
        String thingy = "am";
        int h = Integer.parseInt(split[0]);
        if (h > 12) {
            h -= 12;
            thingy = "pm";
        }

        String f = format.toLowerCase();
        time = h + ":" + split[1]
                + (f.contains("xm") ? (" " + thingy) : (""));

        return time;
    }

    public boolean isUseMeridean() {
        return useMeridean;
    }

    public void setUseMeridean(boolean useMeridean) {
        this.useMeridean = useMeridean;
        useRegularPersonTime();
    }

    private String lastTime = "";

    @Override
    public void update() {
        String pattern = "\\d+(:)?59.*";

        //pattern = ".*";

        Clock me = this;
        String time = getTime();
        if(time.matches(pattern) && !time.equalsIgnoreCase(lastTime)){
            DelayedAction da = new DelayedAction(56000) {
                @Override
                public void action() {
                    updatedPanels.add(me);
                }
            };
            da.deploy();
        }

        lastTime = time;
    }

    @Override
    public boolean isPowered() {
        return true;
    }

    public void reOrient(int newW, int newH){
        super.reOrient(newW,newH);
        configureFont(false);
    }
}
