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
    private ObservableContainer observer;

    public PiPanel(){
        setup();
    }

    public PiPanel(int w, int h) {
        this.w = w;
        this.h = h;
        setup();
    }

    public ObservableContainer getObserver() {
        return observer;
    }

    public void setObserver(ObservableContainer observer) {
        this.observer = observer;
    }

    private void setup(){
        setBackground(Configuration.PANEL_BG_COLOR);
    }

    public abstract void draw(Graphics g);

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
        if(getObserver()!=null){

        }
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
        if(getObserver()!=null){

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
        setW(newW);
        setH(newH);
    }

    public void refit(int oldW, int newW, int oldH, int newH){
        double dw = (double) newW / (double) oldW;
        double dh = (double) newH / (double) oldH;
        refit((int)(dw*getW()),(int)(dh*getH()));
    }
}
