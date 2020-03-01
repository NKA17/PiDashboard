package ui.widget;

import enums.Axis;
import ui.view.PiPanel;

import java.awt.*;

public class Spacer extends PiPanel {
    public Spacer(int size){
        super(size,size);
    }
    public Spacer(int size, Axis axis){
        super(size,size);
        setW(axis==Axis.HORIZONTAL?size:2);
        setH(axis==Axis.VERTICAL?size:2);
    }

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean isPowered() {
        return false;
    }
}
