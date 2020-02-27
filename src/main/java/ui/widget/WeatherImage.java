package ui.widget;

import thirdparty.weather.WeatherData;
import ui.config.Configuration;
import ui.tools.font.FontInfo;
import ui.tools.font.FontTool;
import ui.view.PiPanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WeatherImage extends PiPanel {

    WeatherData weatherData;

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

        BufferedImage icon = weatherData.getIcon();

        String temp = weatherData.getTemperature()+ "°";
        FontInfo fi = FontTool.fitInBox(Configuration.TEXT_FONT,temp,icon.getWidth(),icon.getHeight());

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(fi.getFont());
        g2.setColor(Configuration.TEXT_COLOR);
        g2.drawString(temp,getX()+icon.getWidth()+10,getY()+icon.getHeight());

        g.drawImage(icon,getX(),getY(),null);
    }

    @Override
    public void refresh() {

    }

    @Override
    public boolean isPowered() {
        return false;
    }
}
