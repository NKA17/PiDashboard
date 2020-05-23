package organization;

import config.Configuration;
import raspberryPi.Printer;
import ui.view.PiPanel;
import ui.view.PiScreen;
import ui.widget.Spacer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScreenOrganizer {
    private PiScreen screen;
    private double barRatio = .16;
    private PiPanel main = null;
    private PiPanel currentFocus = null;
    private boolean needToRemoveFromScreenPanels = false;
    private HashMap<PiPanel,Dimension> dimMap = new HashMap();

    public ScreenOrganizer() {

    }

    public PiPanel getCurrentFocus() {
        return currentFocus;
    }

    public void update(){
        List<PiPanel> panels = new ArrayList<>();
        panels.addAll(screen.getPanels());
        panels.addAll(screen.getHiddenPanels());
        panels.addAll(screen.getFooters());
        for(PiPanel p: panels){
            if(!dimMap.containsKey(p)){
                dimMap.put(p,new Dimension(p.getW(),p.getH()));
            }
        }

        if(main == null){
            focus(new Spacer(50));
        }else {
            focus(main);
        }
        fitFooters();
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

        int x = 0;
        int y = 0;
        int biggestH = 0;

        int barSize = (int)((double)screen.getH() * barRatio);
        int provisionalWidth = screen.getW();
        int provisionalHeight = screen.getFooters().size() > 0
                ? screen.getH() - barSize
                : screen.getH();

        for(int i = 0; i < screen.getPanels().size(); i++){
            PiPanel p = screen.getPanels().get(i);
            if(p == panel || !p.isAllowReorganize())continue;

            p.setX(x);
            p.setY(y);

            DimensionNotes dim = fit(p,provisionalWidth,barSize);
            p.setW(dim.neww);
            p.setH(dim.newh);

            x += p.getW();
            biggestH = Math.max(biggestH,p.getH());
        }

        PiPanel p = panel;
        DimensionNotes dim = fit(p,provisionalWidth,provisionalHeight-barSize);
        p.setX(0);
        p.setY(biggestH+Configuration.GRIDBAG_INSETS.top);
        p.setW(screen.getW());
        p.setH(dim.newh-Configuration.GRIDBAG_INSETS.top-Configuration.GRIDBAG_INSETS.bottom);

        if(needToRemoveFromScreenPanels){
            screen.getPanels().remove(currentFocus);
        }

        boolean isHidden = screen.getHiddenPanels().contains(panel);
        needToRemoveFromScreenPanels = !screen.getPanels().contains(panel);
        if(isHidden){
            screen.getPanels().add(panel);
        }
        currentFocus = panel;
        Printer.println("(Organizer) Focusing on {%s}",panel.toString());

        fitFooters();
    }

    public void reset(){
        if(main != null){
            focus(main);
        }
    }

    private DimensionNotes fit(PiPanel panel, int w, int h){
        if(!dimMap.containsKey(panel)){
            dimMap.put(panel,new Dimension(panel.getW(),panel.getH()));
        }

        panel.setW(dimMap.get(panel).getWidth());
        panel.setH(dimMap.get(panel).getHeight());

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

    private void fitFooters(){
        int barSize = (int)((double)screen.getH() * barRatio);
        int x = 0;
        int y = (int)((double)screen.getH()*(1-barRatio));
        for(int i = 0; i < screen.getFooters().size(); i++){
            PiPanel p = screen.getFooters().get(i);

            if(p == currentFocus){
                continue;
            }

            p.setX(x);
            p.setY(y);

            DimensionNotes dim = fit(p,screen.getW(),barSize);
            p.setW(dim.neww);
            p.setH(dim.newh);

            x += p.getW();
        }
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
