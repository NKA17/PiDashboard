package thirdparty.weather;

import graphics.ImageCache;
import thirdparty.ApiClient;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherData {
    private String name = "";
    private String weather = "";
    private int temperature = 0;
    private String windSpeed = "";
    private String windDirection = "";
    private String iconURL = "";
    private String dateTime = "";
    private boolean goodData = true;
    private BufferedImage icon = null;

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public BufferedImage getIcon() {
        if(icon == null){
            ApiClient client = new ApiClient(getIconURL());
            try {
                if(ImageCache.contains(getIconURL())){
                    return ImageCache.get(getIconURL());
                }

                icon = client.getImage("");
                ImageCache.put(getIconURL(),icon);
            }catch (Exception e){
                BufferedImage icon = new BufferedImage(50,50,BufferedImage.TYPE_4BYTE_ABGR);
                icon.getGraphics().setColor(new Color(30,30,30));
                icon.getGraphics().fillRect(0,0,50,50);
                return icon;
            }
        }
        return icon;
    }

    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public boolean equals(WeatherData wd){
        return wd != null
                && wd.getTemperature() == getTemperature()
                && wd.getWeather().equalsIgnoreCase(getWeather())
                && wd.getWindDirection().equalsIgnoreCase(getWindDirection())
                && wd.getWindSpeed().equalsIgnoreCase(getWindSpeed());
    }

    public void update(WeatherData wd){
        if(wd == null){
            return;
        }
        if(!wd.getWeather().equalsIgnoreCase(getWeather())){
            setIcon(null);
        }

        setIconURL(wd.getIconURL());
        setWeather(wd.getWeather());
        setWindSpeed(wd.getWindSpeed());
        setWindDirection(wd.getWindDirection());
        setTemperature(wd.getTemperature());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGoodData() {
        return goodData;
    }

    public void setGoodData(boolean goodData) {
        this.goodData = goodData;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Date getDate(){

        try {
            int index = getDateTime().lastIndexOf("-");
            String date = getDateTime().substring(0,index).replaceAll("T","");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

            Date parsedDate = formatter.parse(date);
//            SimpleDateFormat dayFormat = new SimpleDateFormat("E");
//            return dayFormat.format(parsedDate);
            return parsedDate;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getDayOfWeek(){
        Date date = getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return sdf.format(date);
    }

    /*
    {
        "number": 1,
        "name": "",
        "startTime": "2020-02-26T07:00:00-06:00",
        "endTime": "2020-02-26T08:00:00-06:00",
        "isDaytime": true,
        "temperature": 26,
        "temperatureUnit": "F",
        "temperatureTrend": null,
        "windSpeed": "14 mph",
        "windDirection": "NNW",
        "icon": "https://api.weather.gov/icons/land/day/snow?size=small",
        "shortForecast": "Isolated Snow Showers",
        "detailedForecast": ""
    }
     */
}
