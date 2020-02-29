package organization;

import raspberryPi.RPiInterface;
import realTime.DelayedAction;
import ui.view.PiPanel;
import ui.view.PiScreen;

import java.awt.*;
import java.util.HashMap;

public class ScreenOrganizer {
    private PiScreen screen;
    private double barRatio = .2;
    private PiPanel main = null;

    public ScreenOrganizer() {

    }

    public PiPanel getMainPanel() {
        return main;
    }

    public void setMainPanel(PiPanel main) {
        this.main = main;
    }

    public PiScreen getScreen() {
        return screen;
    }

    public void setScreen(PiScreen screen) {
        this.screen = screen;
    }

    public void focus(PiPanel panel){
        if(!screen.getPanels().contains(panel))return;

        int x = 0;
        int y = 0;
        int biggestH = 0;

        for(int i = 0; i < screen.getPanels().size(); i++){
            PiPanel p = screen.getPanels().get(i);
            if(p == panel || !p.isAllowReorganize())continue;

            p.setX(x);
            p.setY(y);

            DimensionNotes dim = fit(p,screen.getW(),(int)(barRatio*(double)screen.getH()));
            p.setW(dim.neww);
            p.setH(dim.newh);

            x += p.getW();
            biggestH = Math.max(biggestH,p.getH());
        }

        PiPanel p = panel;
        DimensionNotes dim = fit(p,screen.getW(),(int)((1-barRatio)*(double)screen.getH()));
        p.setX(0);
        p.setY(biggestH);
        p.setW(dim.neww);
        p.setH(dim.newh);

        if(main != null && panel != main){
            DelayedAction action = new DelayedAction(30000) {
                @Override
                public void action() {
                    focus(main);
                }
            };
            action.deploy();
        }
    }

    private DimensionNotes fit(PiPanel panel, int w, int h){
        DimensionNotes dn = new DimensionNotes();
        dn.oldw = panel.getW();
        dn.oldh = panel.getH();

        double dh = (double) h / (double) panel.getH();
        double dw = dh;
        dn.dh = dh;

        panel.reOrient((int) ((double) panel.getW() * dh), h);

        if(panel.getW() > w){
            dw = (double) w / (double) panel.getW();
            panel.reOrient((int) ((double) panel.getW() * dw), (int)((double) panel.getH() * dw));
        }

        dn.neww = panel.getW();
        dn.newh = panel.getH();

        return dn;
    }

    private class DimensionNotes{
        int oldw = 0;
        int oldh = 0;
        int neww = 0;
        int newh = 0;
        double dh = 0;

        public String toString(){
            return String.format("%d,%d,%d,%d",oldw,oldh,neww,newh);
        }
    }
}
