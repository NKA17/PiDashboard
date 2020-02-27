package ui.view;

import ui.config.Configuration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PiScreen extends PiPanel {

    private List<PiPanel> panels = new ArrayList<>();

    public PiScreen(int w, int h){
        super(w,h);
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

        screen.setColor(Color.CYAN);
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
        super.refit(newW,newH);
        for(PiPanel pp: panels){
            pp.refit(getW(),getH());
        }
    }
}
