package metrics;

import ui.view.PiPanel;

public class Alignment {
    public enum Anchor{
        LEFT,RIGHT,TOP,BOTTOM,CENTER
    }

    private Anchor horizontal = Anchor.CENTER;
    private Anchor vertical = Anchor.CENTER;

    public Anchor getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(Anchor horizontal) {
        this.horizontal = horizontal;
    }

    public Anchor getVertical() {
        return vertical;
    }

    public void setVertical(Anchor vertical) {
        this.vertical = vertical;
    }
}
