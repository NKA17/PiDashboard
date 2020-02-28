package ui.view;

import realTime.RefreshRateHandler;
import realTime.Refreshable;
import realTime.TimeUnit;
import ui.tools.interfaces.ObservableContainer;
import ui.widget.WeatherImage;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class PiPanel extends JPanel implements Refreshable,ObservableContainer{
    private int w;
    private int h;
    private int x;
    private int y;
    private ObservableContainer observer;
    private RefreshRateHandler refreshRateHandler = null;
    public static Queue<PiPanel> updatedPanels = new ArrayBlockingQueue<PiPanel>(20);
    private boolean allowReorganize = true;

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
        setBackground(null);
    }

    public abstract void draw(Graphics g);

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
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

    public boolean isAllowReorganize() {
        return allowReorganize;
    }

    public void setAllowReorganize(boolean allowReorganize) {
        this.allowReorganize = allowReorganize;
    }

    public void reOrient(int newW, int newH){
        //setProportions(newW,newH);
        setW(newW);
        setH(newH);
    }

    public void reOrient(int oldW, int newW, int oldH, int newH){
        double dw = (double) newW / (double) oldW;
        double dh = (double) newH / (double) oldH;

        setX((int)(getX()*dw));
        setY((int)(getY()*dh));

        reOrient((int)(dw*getW()),(int)(dh*getH()));
    }

    public abstract void update();

    public final void refresh(){
        if(refreshRateHandler != null && refreshRateHandler.isTimeToRefresh()){
            update();
        }
    }
    public void setRefreshInterval(long interval){
        refreshRateHandler = new RefreshRateHandler(interval);
        play();
    }
    public void setRefreshInterval(long interval, TimeUnit tu){
        switch (tu){
            case HOURS:
                interval *= 60;
            case MINUTES:
                interval *= 60;
            case SECONDS:
                interval *= 1000;
        }
        setRefreshInterval(interval);
    }
    public void play(){
        if(refreshRateHandler == null)return;
        refreshRateHandler.play();
    }
    public void pause(){
        if(refreshRateHandler == null)return;
        refreshRateHandler.pause();
    }
}
