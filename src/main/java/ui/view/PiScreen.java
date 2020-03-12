package ui.view;

import organization.ScreenOrganizer;
import raspberryPi.Printer;
import raspberryPi.RPiInterface;
import realTime.DelayedAction;
import config.Configuration;
import ui.tools.interfaces.ObservableContainer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class PiScreen extends PiPanel {

    private List<PiPanel> panels = new ArrayList<>();
    private List<PiPanel> footers = new ArrayList<>();
    private List<PiPanel> hiddenPanels = new ArrayList<>();
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

        screen.setColor(Configuration.SCREEN_BG_COLOR);
        screen.fillRect(0,0, getW(), getH());

        try {
            for (PiPanel p : panels) {
                p.draw(screen);
                //screen.setColor(Color.RED);
                //screen.drawRect(p.getX(),p.getY(),p.getW()-1,p.getH()-1);
            }
            for(PiPanel p: footers){
                p.draw(screen);
                //screen.setColor(Color.RED);
                //screen.drawRect(p.getX(),p.getY(),p.getW()-1,p.getH()-1);
            }
        }catch (ConcurrentModificationException e){
            //well shoot.
        }

        g.drawImage(screenImage,0,0,null);
    }

    @Override
    public void update() {
        try{
            for(PiPanel pp: hiddenPanels){
                pp.refresh();
            }
            for(PiPanel pp: panels){
                pp.refresh();
            }
            for(PiPanel pp: footers){
                pp.refresh();
            }
            if(!PiPanel.isQueueEmpty()){
                emptyQueue();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private long lastEmptied = System.currentTimeMillis();
    private void emptyQueue(){
        if(System.currentTimeMillis() - lastEmptied < Configuration.SWAP_TIME){
            return;
        }

        lastEmptied = System.currentTimeMillis();

        PiPanel panel = PiPanel.pollFromUpdateQueue();
        Printer.println("A panel updated {}",panel.toString());
        if(organizer != null){
            organizer.focus(panel);
        }
        RPiInterface.wakeScreen();

        if(PiPanel.isQueueEmpty()){
            if(Configuration.SLEEP_AFTER_TIME != -1) {
                DelayedAction action = new DelayedAction(Configuration.SLEEP_AFTER_TIME) {
                    @Override
                    public void action() {
                        if(PiPanel.isQueueEmpty()) {
                            RPiInterface.sleepScreen();
                        }
                    }
                };
                action.setDescription("Sleep screen after showing updates");
                action.deploy();
            }
            if(Configuration.SWAP_TIME != -1){
                DelayedAction action = new DelayedAction(Configuration.SWAP_TIME) {
                    @Override
                    public void action() {
                        organizer.reset();
                    }
                };
                action.setDescription("No more updates to show, reset organizer");
                action.deploy();
            }
        }
//        else{
//            DelayedAction action = new DelayedAction(Configuration.SWAP_TIME) {
//                @Override
//                public void action() {
//                    emptyQueue();
//                }
//            };
//            action.deploy();
//        }
    }

    public void addPiPanel(PiPanel panel){
        getPanels().add(panel);
        panel.setObserver(this);
        if(organizer != null){
            organizer.update();
        }
    }
    public void addPiPanel(PiPanel panel, int index){
        getPanels().add(index,panel);
        panel.setObserver(this);
        if(organizer != null){
            organizer.update();
        }
    }

    public void removePiPanel(PiPanel panel){
        getPanels().remove(panel);
        panel.setObserver(null);
        if(organizer != null){
            organizer.update();
        }
    }

    public void addFooterPanel(PiPanel panel){
        getFooters().add(panel);
        panel.setObserver(this);
        if(organizer != null){
            organizer.update();
        }
    }

    public void addFooterPanel(PiPanel panel, int index){
        getFooters().add(index, panel);
        panel.setObserver(this);
        if(organizer != null){
            organizer.update();
        }
    }

    public void removeFooterPanel(PiPanel panel){
        getFooters().remove(panel);
        panel.setObserver(null);
        if(organizer != null){
            organizer.update();
        }
    }

    public void addHiddenPanel(PiPanel panel){
        getHiddenPanels().add(panel);
        panel.setObserver(this);
        if(organizer != null){
            organizer.update();
        }
    }

    public List<PiPanel> getFooters() {
        return footers;
    }

    public void setFooters(List<PiPanel> footers) {
        this.footers = footers;
    }

    public List<PiPanel> getHiddenPanels() {
        return hiddenPanels;
    }

    public void setHiddenPanels(List<PiPanel> hiddenPanels) {
        this.hiddenPanels = hiddenPanels;
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
        for(PiPanel pp: footers){
            pp.reOrient(oldW,getW(),oldH,getH());
        }
    }
}
