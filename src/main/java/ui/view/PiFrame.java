package ui.view;

import realTime.Refreshable;
import realTime.Ticker;
import config.Configuration;
import ui.tools.interfaces.ObservableContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PiFrame extends JFrame implements Refreshable,ObservableContainer {

    private List<PiScreen> screens = new ArrayList<>();
    private JPanel drawPanel;

    public PiFrame(){
        setDimensions();
        setColor();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        drawPanel = new JPanel();
        drawPanel.setPreferredSize(new Dimension(getW(),getH()));
        getContentPane().add(drawPanel);

        createResizeListener();
    }

    public PiScreen createScreen(int w, int h){
        PiScreen screen = new PiScreen(w,h,this);
        getContentPane().add(screen);
        screens.add(screen);
        return screen;
    }

    public List<PiScreen> getScreens() {
        return screens;
    }

    public void setScreens(List<PiScreen> screens) {
        this.screens = screens;
    }

    public void setColor(){
        setBackground(Configuration.WINDOW_BG_COLOR);
        getContentPane().setBackground(Configuration.WINDOW_BG_COLOR);
    }

    public void setDimensions(){
        Dimension dim = new Dimension(500,300);
        if(Configuration.HEIGHT_CONSTRAINT > 0){
            dim = new Dimension(Configuration.WIDTH_CONSTRAINT,Configuration.HEIGHT_CONSTRAINT);
        }

        setPreferredSize(new Dimension((int)dim.getWidth(),(int)dim.getHeight()));
        setSize(new Dimension((int)dim.getWidth(),(int)dim.getHeight()));
        getContentPane().setPreferredSize(dim);
        getContentPane().setSize(dim);
    }

    @Override
    public void refresh() {
        for(PiScreen s: screens){
            s.refresh();
            s.repaint();
        }
    }

    @Override
    public boolean isPowered() {
        return true;
    }

    public void timer(int interval){
        Ticker ticker = new Ticker(interval, this);
        Thread th = new Thread(ticker);
        th.start();
        for(PiScreen ps: screens){
            ps.setRefreshInterval(interval);
        }
    }

    private void createResizeListener(){
        ComponentListener cl = new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                for(PiScreen screen: screens) {
                   // screen.reOrient(e.getComponent().getWidth(), e.getComponent().getHeight());
                    screen.reOrient(getContentPane().getWidth(), getContentPane().getHeight());
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        };
        addComponentListener(cl);
    }

    @Override
    public int getW() {
        return getWidth();
    }

    @Override
    public int getH() {
        return getHeight();
    }

    public String toString(){

        return String.format("%s[x=%d,y=%d,w=%d,h=%d]",
                getClass().getName(),getX(),getY(),getW(),getH());
    }
}
