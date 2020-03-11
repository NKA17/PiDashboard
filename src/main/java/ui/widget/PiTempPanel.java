package ui.widget;

import config.Configuration;
import raspberryPi.RPiInterface;
import realTime.DelayedAction;
import ui.tools.font.FontInfo;
import ui.tools.font.FontTool;
import ui.view.PiPanel;

import java.awt.*;

public class PiTempPanel extends PiPanel {

    private enum Status{
        SAFE,WARNING,DANGER,OH_SHIT
    }

    public PiTempPanel(){
        super(60,30);
        setFont(new Font(getFont().getFontName(),Font.BOLD,getFont().getSize()));
    }

    private double temp = RPiInterface.getTemperature();
    private double[] history = new double[20];
    private Color safe = new Color(0,200,100);
    private Color warning = new Color(200,200,100);
    private Color danger = new Color(200,50,75);
    private int dangerThreshold = 80;
    private int warningTrheshold = 73;
    private int ohShitThreshold = 84;
    private Status status = getStatus(temp);

    @Override
    public void draw(Graphics g) {
        int L = Configuration.GRIDBAG_INSETS.left;
        int R = Configuration.GRIDBAG_INSETS.right;
        int T = Configuration.GRIDBAG_INSETS.top;
        int B = Configuration.GRIDBAG_INSETS.bottom;

        if(status == Status.OH_SHIT){
            FontInfo fi = FontTool.fitInBox(getFont(),"Oh FUCK.",getW()-L-R,getH()-T-B);
            g.setColor(Configuration.TEXT_COLOR);
            g.setFont(fi.getFont());
            FontTool.drawAAString(g,"Oh FUCK.",getX()+R,getY()+T);
            return;
        }

        int ww = getW()-L-R;
        int hh = getH()-T-B;

        int x = getX()+L;
        int dx = ww / history.length;
        int y = getY()+T+hh;

        Polygon p = new Polygon();
        for(int i = 0; i < history.length; i++){
            p.addPoint(x,(int)(y - (history[i]/100)*hh));
            if(history[i] > warningTrheshold){
                g.setColor(getColor(getStatus(history[i])));
                g.drawLine(x,(int)(y - (history[i]/100)*hh),x,y);
            }
            x+=dx;
        }
        x-=dx;
        p.addPoint(x,y);
        p.addPoint(getX()+L,y);

        Color c = getColor(status);
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),150));
        g.fillPolygon(p);
        g.setColor(c);
        g.drawPolygon(p);

        Polygon rect = new Polygon();
        rect.addPoint(getX()+L,getY()+T);
        rect.addPoint(x,getY()+T);
        rect.addPoint(x,y);
        rect.addPoint(getX()+L,y);
        g.drawPolygon(rect);

        FontInfo f1 = FontTool.fitInBox(getFont(),"55.5°",(x)-(getX()+L),(int)(hh*(1.0/2.0)));

        g.setFont(f1.getFont());
        g.setColor(Configuration.TEXT_COLOR);
        //FontTool.drawAAString(g,"temp",getX()+L+L,getY()+T+T);
        FontTool.drawAAString(g,String.format("%.1f°",temp),getX()+L+L,y-f1.getH()-B);
    }

    private Color getColor(Status status){
        switch (status){
            case SAFE:
                return safe;
            case WARNING:
                return warning;
            case DANGER:
                return danger;
            case OH_SHIT:
                return danger;
        }
        return safe;
    }

    @Override
    public void update() {
        temp = RPiInterface.getTemperature();
        Status oldStatus = status;
        status = getStatus(temp);

        if(oldStatus != status || status == Status.DANGER){
            //PiPanel.addToUpdateQueue(this);
        }

        if(status == Status.DANGER){
            DelayedAction da = new DelayedAction(2000) {
                @Override
                public void action() {
                    RPiInterface.sleepScreen();
                }
            };
            da.deploy();
        }else
        if(status == Status.OH_SHIT && oldStatus != Status.OH_SHIT){
            while(!PiPanel.isQueueEmpty()){
                PiPanel.pollFromUpdateQueue();
            }
            PiPanel.addToUpdateQueue(this);
            RPiInterface.emergencyTerminate(3000, String.format("Temp reached %d°C",temp));
        }

        for(int i = 0; i < history.length-1; i++){
            history[i] = history[i+1];
        }
        history[history.length - 1] = temp;
    }

    private Status getStatus(double temp){
        if(temp > ohShitThreshold){
            return Status.OH_SHIT;
        }
        if(temp > dangerThreshold){
            return Status.DANGER;
        }
        if(temp > warningTrheshold){
            return Status.WARNING;
        }
        return Status.SAFE;
    }

    @Override
    public boolean isPowered() {
        return false;
    }
}
