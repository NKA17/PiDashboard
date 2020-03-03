package ui.widget;

import enums.Axis;
import graphics.ImageCache;
import graphics.sprites.SpriteSheet;
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
import java.io.BufferedReader;

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
        int L = Configuration.GRIDBAG_INSETS.left;
        int R = Configuration.GRIDBAG_INSETS.right;
        int T = Configuration.GRIDBAG_INSETS.top;
        int B = Configuration.GRIDBAG_INSETS.bottom;

        //yes, using twice H here because the height is the limiter
        BufferedImage icon = loadIcon(getH(),getH());

        int margin = 10;
        Rectangle textRect = new Rectangle();
        textRect.width = getW()-icon.getWidth()-L-R;
        textRect.height = (getH()/2)-T-B;

        String temp = weatherData.getTemperature()+ "°";
        String wind = "wind "+weatherData.getWindSpeed().replaceAll("\\D","");

        FontInfo tempFont = FontTool.fitInBox(Configuration.TEXT_FONT,temp,textRect.width,textRect.height);
        FontInfo windFont = FontTool.fitInBox(Configuration.TEXT_FONT,wind,textRect.width,textRect.height);

        g.setColor(Configuration.TEXT_COLOR);
        g.setFont(tempFont.getFont());
        FontTool.drawAAString(g,temp,getX()+icon.getWidth()+L,getY()+T);
        g.setFont(windFont.getFont());
        FontTool.drawAAString(g,wind,getX()+icon.getWidth()+L,getY()+T+B+T+textRect.height);

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
        if(newData == null){
            return;
        }

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
