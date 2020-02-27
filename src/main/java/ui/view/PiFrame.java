package ui.view;

import realTime.Refreshable;
import realTime.Ticker;
import ui.config.Configuration;
import ui.tools.interfaces.ObservableContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PiFrame extends JFrame implements Refreshable,ObservableContainer {

    private PiScreen screenPanel;

    public PiFrame(){
        setDimensions();
        setColor();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        screenPanel = new PiScreen(getWidth(),getHeight());
        screenPanel.setW(100);
        screenPanel.setH(100);
        screenPanel.setObserver(this);
        getContentPane().add(screenPanel);

        createResizeListener();
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

        setPreferredSize(dim);
        setSize(dim);
        getContentPane().setPreferredSize(dim);
        getContentPane().setSize(dim);
    }

    @Override
    public void refresh() {
        getScreenPanel().refresh();
        screenPanel.repaint();
    }

    @Override
    public boolean isPowered() {
        return true;
    }

    public void timer(int interval){
        Ticker ticker = new Ticker(interval, this);
        Thread th = new Thread(ticker);
        th.start();
    }

    public void addPiPanel(PiPanel panel){
        getScreenPanel().addPiPanel(panel);
    }

    public PiScreen getScreenPanel() {
        return screenPanel;
    }

    public void setScreenPanel(PiScreen screenPanel) {
        this.screenPanel = screenPanel;
    }

    private void createResizeListener(){
        ComponentListener cl = new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                getScreenPanel().refit(e.getComponent().getWidth(),e.getComponent().getHeight());
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
}
