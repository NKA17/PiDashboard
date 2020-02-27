package thirdparty.weather;

import org.json.JSONArray;
import org.json.JSONObject;
import thirdparty.ApiClient;

public class WeatherAPI {

    ApiClient client = new ApiClient("https://api.weather.gov/");

    public WeatherData getWeatherNow(){
        String endPoint;

        endPoint = getCoordinatesEndpoint();
        endPoint = getGridPointsEndPoint(endPoint);

        String response = client.makeRequest(endPoint);
        JSONObject json = new JSONObject(response);
        JSONObject properties = json.getJSONObject("properties");
        JSONArray periods = properties.getJSONArray("periods");
        JSONObject now = periods.getJSONObject(0);

        WeatherData wd = new WeatherData();
        wd.setWeather(now.getString("shortForecast"));
        wd.setTemperature(now.getInt("temperature"));
        wd.setWindSpeed(now.getString("windSpeed"));
        wd.setWindDirection(now.getString("windDirection"));
        wd.setIconURL(now.getString("icon").replaceAll("size=small","size=large"));

        return wd;
    }

    public String getCoordinatesEndpoint(){
        return "https://api.weather.gov/points/41.572,-93.7453";
    }

    private String getGridPointsEndPoint(String endPoint){
        //https://api.weather.gov/points/41.572,-93.7453
        String response = client.makeRequest(endPoint);
        JSONObject json = new JSONObject(response);
        JSONObject properties = json.getJSONObject("properties");
        return properties.getString("forecastHourly");
    }

    public static void main(String[] args){
        WeatherAPI api = new WeatherAPI();
        System.out.println(api.getWeatherNow().getTemperature());
    }
}
