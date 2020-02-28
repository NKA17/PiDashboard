package ui.widget;

import raspberryPi.RPiInterface;
import realTime.Refreshable;
import realTime.Ticker;
import realTime.TimeUnit;
import thirdparty.weather.WeatherAPI;
import thirdparty.weather.WeatherData;
import ui.config.Configuration;
import ui.tools.font.FontInfo;
import ui.tools.font.FontTool;
import ui.tools.graphics.ImageTransform;
import ui.view.PiPanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WeatherImage extends PiPanel {

    private WeatherData weatherData = null;
    private boolean update = false;

    public WeatherImage(int w, int h) {
        super(w, h);
    }

    @Override
    public void draw(Graphics g) {
        if(weatherData == null){
            return;
        }

        //yes, using H here because the height is the limiter
        BufferedImage icon = loadIcon(getH(),getH());

        int margin = 10;

        String temp = weatherData.getTemperature()+ "°";
        FontInfo fi = FontTool.fitInBox(
                Configuration.TEXT_FONT,
                temp,
                getW()-icon.getWidth()-margin,
                icon.getHeight());

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(fi.getFont());
        g2.setColor(Configuration.TEXT_COLOR);
        g2.drawString(temp,getX()+icon.getWidth()+margin,getY()+icon.getHeight()-margin);

        g.drawImage(icon,getX(),getY(),null);
    }

    private BufferedImage loadIcon(int w, int h){
        BufferedImage image = weatherData.getIcon();
        if(image.getWidth() != w || image.getHeight() != h){
            image = ImageTransform.resize(image,w,h);
        }

        return image;
    }

    public void updateAfter(int interval, TimeUnit tu){
        if(tu == TimeUnit.MILLISECONDS){
            pause();
        }else {
            setRefreshInterval(interval,tu);
        }
    }

    public void updateAfterMinutes(int interval){
        if(interval <= 0){
            pause();
        }else {
            setRefreshInterval(interval, TimeUnit.MINUTES);
        }
    }

    int i = 0;
    @Override
    public void update() {
        WeatherAPI client = new WeatherAPI();
        WeatherData newData = client.getWeatherNow();
        //newData.setTemperature(newData.getTemperature()+(i++));
        if(!newData.equals(weatherData)){
            if(weatherData == null){
                weatherData = newData;
            }else {
                weatherData.update(newData);
            }
            updatedPanels.add(this);
        }
    }

    @Override
    public boolean isPowered() {
        return update;
    }
}
