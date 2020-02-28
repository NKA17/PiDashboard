package ui.view;

import organization.ScreenOrganizer;
import raspberryPi.RPiInterface;
import realTime.DelayedAction;
import ui.config.Configuration;
import ui.tools.interfaces.ObservableContainer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class PiScreen extends PiPanel {

    private List<PiPanel> panels = new ArrayList<>();
    private double dw = 1;
    private double dh = 1;
    private ScreenOrganizer organizer;

    public PiScreen(int w, int h, ObservableContainer observer){
        super(w,h);
        setObserver(observer);

        dw = (double)getW() / (double)observer.getW();
        dh = (double)getH() / (double)observer.getH();

        setBackground(Configuration.PANEL_BG_COLOR);
    }

    public ScreenOrganizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(ScreenOrganizer organizer) {
        if(this.organizer != null) {
            this.organizer.setScreen(null);
        }
        this.organizer = organizer;
        organizer.setScreen(this);
    }

    public void paintComponent(Graphics g){
        draw(g);
    }

    public List<PiPanel> getPanels() {
        return panels;
    }

    public void setPanels(List<PiPanel> panels) {
        this.panels = panels;
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage screenImage = new BufferedImage(getW(), getH(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics screen = screenImage.getGraphics();

        screen.setColor(getBackground());
        screen.fillRect(0,0, getW(), getH());

        try {
            for (PiPanel p : panels) {
                p.draw(screen);
                //screen.setColor(Color.CYAN);
                //screen.drawRect(p.getX(),p.getY(),p.getW(),p.getH());
            }
        }catch (ConcurrentModificationException e){
            //well shoot.
        }

        g.drawImage(screenImage,0,0,null);
    }

    @Override
    public void update() {
        for(PiPanel pp: panels){
            pp.refresh();
        }
        if(!updatedPanels.isEmpty()){
            emptyQueue();
        }
    }

    private void emptyQueue(){
        PiPanel panel = updatedPanels.poll();
        if(organizer != null){
            organizer.focus(panel);
        }
        RPiInterface.wakeScreen();

        if(updatedPanels.isEmpty()){
            if(Configuration.WAKE_SCREEN_TIME != -1) {
                DelayedAction action = new DelayedAction(Configuration.WAKE_SCREEN_TIME) {
                    @Override
                    public void action() {
                        if(PiPanel.updatedPanels.isEmpty()) {
                            RPiInterface.sleepScreen();
                        }
                    }
                };
                action.deploy();
            }
        }else{
            DelayedAction action = new DelayedAction(Configuration.SWAP_TIME) {
                @Override
                public void action() {
                    emptyQueue();
                }
            };
            action.deploy();
        }
    }

    public void addPiPanel(PiPanel panel){
        getPanels().add(panel);
        panel.setObserver(this);
    }

    @Override
    public boolean isPowered() {
        return false;
    }

    public void reOrient(int newW, int newH){
        int oldW = getW();
        int oldH = getH();

        super.reOrient((int)(dw*newW),(int)(dh*newH));
        for(PiPanel pp: panels){
            pp.reOrient(oldW,getW(),oldH,getH());
        }
    }
}
