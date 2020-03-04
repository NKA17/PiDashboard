package ui.widget;

import thirdparty.location.LocationData;
import config.Configuration;
import ui.tools.font.FontTool;
import ui.view.PiPanel;

import java.awt.*;

public class LocationDisplay extends PiPanel {

    public LocationDisplay() {
        super();
    }

    public LocationDisplay(int w, int h) {
        super(w, h);
    }

    @Override
    public void draw(Graphics g) {

        int L = Configuration.GRIDBAG_INSETS.left;
        int R = Configuration.GRIDBAG_INSETS.right;
        int T = Configuration.GRIDBAG_INSETS.top;
        int B = Configuration.GRIDBAG_INSETS.bottom;

        LocationData data = Configuration.LOCATION_DATA;
        Rectangle textRect = new Rectangle(0,0,getW()-L-R,(getH()/2)-T-B);

        if(data == null){

        }else {
            String city = String.format("%s, %s",data.getCity(),data.getRegion());
            String country = data.getCountryName();
            Font cityFont = FontTool.fitInBox(Configuration.TEXT_FONT, city, textRect.width, textRect.height).getFont();
            Font countryFont = FontTool.fitInBox(Configuration.TEXT_FONT, country, textRect.width, textRect.height).getFont();

            g.setColor(Configuration.TEXT_COLOR);
            g.setFont(cityFont);
            FontTool.drawAAString(g,city,getX()+L,getY()+T);
            g.setFont(countryFont);
            FontTool.drawAAString(g,country,getX()+L,getY()+(getH()/2)+T);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isPowered() {
        return false;
    }
}
