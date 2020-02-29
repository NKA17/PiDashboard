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
    private Font font;
    private String format = "H:mm";
    private boolean useMeridean = true;

    public void useRegularPersonTime(){
        format = format.split(" ")[0]+ (useMeridean ? " xm" : "");
    }

    public void useMilitaryTime(){
        format = "HHmm";
    }

    public Clock(int w, int h){
        super(w,h);
        font = Configuration.TEXT_FONT;
        setForeground(Configuration.TEXT_COLOR);
        setRefreshInterval(1000);
        inheritConfiguration();
    }

    public void inheritConfiguration(){
        if(Configuration.CLOCK_MILITARY){
            useMilitaryTime();
        }else{
            setUseMeridean(Configuration.CLOCK_MERIDIAN);
        }
    }

    @Override
    public void draw(Graphics g) {
        int L = Configuration.GRIDBAG_INSETS.left;
        int R = Configuration.GRIDBAG_INSETS.right;
        int T = Configuration.GRIDBAG_INSETS.top;
        int B = Configuration.GRIDBAG_INSETS.bottom;

        String time = getTime();
        FontInfo fontInfo = time.split(":")[0].matches("\\d\\d")
                ? FontTool.fitInBox(font,time,getW()-L-R,getH()-T-B)
                : FontTool.fitInBox(font,"1"+time,getW()-L-R,getH()-T-B);
        int dw = time.split(":")[0].matches("\\d\\d")
                ? 0
                : FontTool.getBounds(fontInfo.getFont(),"1").getW();

        //g.setColor(Color.CYAN);
        //g.drawRect(getX()+L,getY()+T,fontInfo.getW(),fontInfo.getH());

        int centerOffset = (getH()-fontInfo.getH()) / 2;
        g.setColor(Configuration.TEXT_COLOR);
        g.setFont(fontInfo.getFont());
        FontTool.drawAAString(g,time,getX()+L+dw,getY()+centerOffset);
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
            DelayedAction da = new DelayedAction(57000) {
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
    }
}
