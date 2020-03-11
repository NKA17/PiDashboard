package ui.widget;

import config.Configuration;
import thirdparty.weather.WeatherAPI;
import thirdparty.weather.WeatherData;
import ui.tools.font.FontInfo;
import ui.tools.font.FontTool;
import ui.tools.graphics.ImageTransform;
import ui.view.PiPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class WeatherForecast extends PiPanel {

    private List<WeatherData> data = new ArrayList<>();
    private int days = 4;
    private String format = "%s, %d°, %s wind";

    public WeatherForecast(int w, int h){
        super(w,h);
        WeatherAPI client = new WeatherAPI();
        data = client.getBadWeatherForecastDays(days);
    }

    @Override
    public void draw(Graphics g) {

        int L = Configuration.GRIDBAG_INSETS.left;
        int R = Configuration.GRIDBAG_INSETS.right;
        int T = Configuration.GRIDBAG_INSETS.top;
        int B = Configuration.GRIDBAG_INSETS.bottom;

        Rectangle rect = new Rectangle();
        rect.width = getW()-L-R;
        rect.height = getH()/days - T - B;

        setFont(new Font(Configuration.TEXT_FONT.getName(),
                Configuration.TEXT_FONT.getStyle(),
                getSmallestFont(rect.width-L-R-(rect.height-T-B),rect.height)));

        int x = getX();
        int y = getY();
        for(int i = 0; i < days; i++){
            WeatherData d = data.get(i);
            if(i > 0){
                //g.drawLine(x,y,x+getW(),y);
            }
            g.drawImage(drawRow(d,rect.width,rect.height),x,y,null);
            y += getH()/days;
        }
    }

    private int getSmallestFont(int w, int h){
        int smallestFont = Configuration.TEXT_FONT.getSize();
        int L = Configuration.GRIDBAG_INSETS.left;
        int R = Configuration.GRIDBAG_INSETS.right;
        int T = Configuration.GRIDBAG_INSETS.top;
        int B = Configuration.GRIDBAG_INSETS.bottom;

        for(WeatherData d: data){
            String t = String.format(format,d.getName(), d.getTemperature(),d.getWindSpeed() );
            FontInfo tempFont = FontTool.fitInBox(Configuration.TEXT_FONT,t,w-L-R,h-T-B);
            smallestFont = Math.min(tempFont.getSize(),smallestFont);
        }

        return smallestFont;
    }

    private BufferedImage drawRow(WeatherData data, int w, int h){
        if(data == null){
            BufferedImage icon = new BufferedImage(w,h,BufferedImage.TYPE_4BYTE_ABGR);
            icon.getGraphics().setColor(new Color(30,30,30));
            icon.getGraphics().fillRect(0,0,w,h);
            return icon;
        }
        int L = Configuration.GRIDBAG_INSETS.left;
        int R = Configuration.GRIDBAG_INSETS.right;
        int T = Configuration.GRIDBAG_INSETS.top;
        int B = Configuration.GRIDBAG_INSETS.bottom;

        BufferedImage canvas = new BufferedImage(w,h,BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = canvas.getGraphics();

        //yes, using twice H here because the height is the limiter
        BufferedImage icon = loadIcon(data,h,h);

        int margin = 10;

        String temp = String.format(format,data.getName(), data.getTemperature(),data.getWindSpeed() );

        int columnWidth = (w - icon.getWidth()) / 3 +L*3;

        g.setColor(Configuration.TEXT_COLOR);
        g.setFont(getFont());
        FontTool.drawAAString(g,data.getName(),icon.getWidth()+L*3,0);
        FontTool.drawAAString(g,data.getTemperature()+"°",(columnWidth)+L*3,0);
        FontTool.drawAAString(g,data.getWindSpeed()+" mph wind",(columnWidth)*2+L*3,0);

        g.drawImage(icon,L,T,null);

        return canvas;
    }

    private BufferedImage loadIcon(WeatherData data, int w, int h){
        BufferedImage image = data.getIcon();
        if(image.getWidth() != w || image.getHeight() != h){
            image = ImageTransform.resize(image,w,h);
        }
        return image;
    }

    @Override
    public void update() {
        WeatherAPI client = new WeatherAPI();
        List<WeatherData> newData = client.getWeatherForecastDays(days);
        if(newData.size() == 0){
            return;
        }

        boolean changed = false;
        for(int i = 0; i < newData.size() && i < data.size(); i++){
            changed |= !data.get(i).equals(newData.get(i));
        }

        data = newData;
        if(changed){
            PiPanel.addToUpdateQueue(this);
        }
    }

    @Override
    public boolean isPowered() {
        return false;
    }
}
