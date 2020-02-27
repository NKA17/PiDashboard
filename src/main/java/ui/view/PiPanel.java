package ui.view;

import realTime.Refreshable;
import ui.config.Configuration;
import ui.tools.interfaces.ObservableContainer;

import javax.swing.*;
import java.awt.*;

public abstract class PiPanel extends JPanel implements Refreshable,ObservableContainer{
    private int w;
    private int h;
    private int x;
    private int y;
    private double dw = -1;
    private double dh = -1;
    private ObservableContainer observer;

    public PiPanel(){
        dw = 1;
        dh = 1;
        setup();
    }

    public PiPanel(int w, int h) {
        this.w = w;
        this.h = h;
        setProportions(w,h);
        setup();
    }

    public ObservableContainer getObserver() {
        return observer;
    }

    public void setObserver(ObservableContainer observer) {
        this.observer = observer;
        setProportions(observer.getW(),observer.getH());
    }

    public void setProportions(int ow, int oh){
        dw = (double)getW() / (double)ow;
        dh = (double)getH() / (double)oh;
    }

    private void setup(){
        setDimensions();
        setBackground(Configuration.PANEL_BG_COLOR);
    }

    public void setDimensions(){
        setW(w*dw);
        setH(h*dh);
        setPreferredSize(new Dimension(getW(),getH()));
    }

    public abstract void draw(Graphics g);

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
        if(getObserver()!=null){
            this.dw = (double)getObserver().getW() / (double)w;
        }
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
        if(getObserver()!=null){
            this.dh = (double)getObserver().getH() / (double)h;
        }
    }

    public void setW(double w){
        setW((int)w);
    }

    public void setH(double h){
        setH((int)h);
    }

    @Override
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void refit(int newW, int newH){
        //setProportions(newW,newH);
        setW(dw * newW);
        setH(dh * newH);
    }
}
