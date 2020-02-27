package ui.widget;

import raspberryPi.RPiInterface;
import realTime.Refreshable;
import realTime.Ticker;
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

    private WeatherData weatherData;
    private boolean update = false;

    public WeatherImage(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public WeatherImage(int w, int h, WeatherData weatherData) {
        super(w, h);
        this.weatherData = weatherData;
    }

    @Override
    public void draw(Graphics g) {
//        int usex = getX() + (getW()/2);
//        usex -= (weatherData.getIcon().getWidth()/2);
//
//        int usey = getY() + (getH()/2);
//        usey -= (weatherData.getIcon().getHeight()/2);

        //yes, using H here because the height is the limiter
        BufferedImage icon = loadIcon(getH(),getH());

        int margin = (int)(.2*(double)getH());

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

    public void updateAfterMinutes(int minutes){
        if(minutes <= 0){
            pause();
        }else {
            setRefreshInterval(250*minutes);
        }
    }

    int i = 0;
    @Override
    public void update() {
        WeatherAPI client = new WeatherAPI();
        WeatherData newData = client.fakeIt();
        newData.setTemperature(newData.getTemperature()+(i++));
        if(!weatherData.equals(newData)){
            weatherData.update(newData);
            RPiInterface.wakeScreen(30000);
        }
    }

    @Override
    public boolean isPowered() {
        return update;
    }
}
