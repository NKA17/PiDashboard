package ui.view;

import ui.config.Configuration;
import ui.tools.interfaces.ObservableContainer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PiScreen extends PiPanel {

    private List<PiPanel> panels = new ArrayList<>();
    private double dw = 1;
    private double dh = 1;

    public PiScreen(int w, int h, ObservableContainer observer){
        super(w,h);
        setObserver(observer);

        dw = (double)getW() / (double)observer.getW();
        dh = (double)getH() / (double)observer.getH();

        setBackground(Configuration.WINDOW_BG_COLOR);
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
        screen.fillRect(0,0,getW(),getH());

        for(PiPanel p: panels){
            p.draw(screen);
        }

        g.drawImage(screenImage,0,0,null);
    }

    @Override
    public void refresh() {
        for(PiPanel pp: panels){
            pp.refresh();
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

    public void refit(int newW, int newH){
        int oldW = getW();
        int oldH = getH();

        super.refit((int)(dw*newW),(int)(dh*newH));
        for(PiPanel pp: panels){
            pp.refit(oldW,getW(),oldH,getH());
        }
    }
}
