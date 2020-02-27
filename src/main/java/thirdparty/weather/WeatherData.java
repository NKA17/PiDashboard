package thirdparty.weather;

import thirdparty.ApiClient;

import java.awt.image.BufferedImage;

public class WeatherData {
    private String weather = "";
    private int temperature = 0;
    private String windSpeed = "";
    private String windDirection = "";
    private String iconURL = "";
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
            icon = client.getImage("");
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
