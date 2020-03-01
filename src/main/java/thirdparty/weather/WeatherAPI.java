package thirdparty.weather;

import org.json.JSONArray;
import org.json.JSONObject;
import raspberryPi.Printer;
import raspberryPi.RPiInterface;
import thirdparty.ApiClient;
import thirdparty.location.LocationApi;
import thirdparty.location.LocationData;
import ui.config.Configuration;

import java.io.IOException;

public class WeatherAPI {

    ApiClient client = new ApiClient("https://api.weather.gov/");

    public WeatherData fakeIt(){
        WeatherData data = new WeatherData();
        data.setWeather("Cloudy");
        data.setTemperature(27);
        data.setIconURL("https://api.weather.gov/icons/land/night/sct?size=small");
        return data;
    }
    public WeatherData getWeatherNow(){
        Printer.println("MAKING REQUEST!!!");

        try {
            String endPoint;

            endPoint = getCoordinatesEndpoint();
            if(endPoint == null){
                return getBadData();
            }
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
        }catch (IOException e){
            return getBadData();
        }
    }

    public String getCoordinatesEndpoint(){
        String format = "https://api.weather.gov/points/%s,%s";
        if(Configuration.LOCATION_LONGITUDE != null && Configuration.LOCATION_LATITUDE != null){
            return String.format(format,Configuration.LOCATION_LATITUDE,Configuration.LOCATION_LONGITUDE);
        }
        if(Configuration.LOCATION_PUBLIC_IP != null){
            LocationApi _client = new LocationApi();
            LocationData data = _client.getLocationDataByIpAddress(Configuration.LOCATION_PUBLIC_IP);
            if(data == null){
                return null;
            }
            Configuration.LOCATION_LONGITUDE = data.getLongitude()+"";
            Configuration.LOCATION_LATITUDE = data.getLatitude()+"";
            return String.format(format,data.getLatitude(),data.getLongitude());
        }

        String ip = RPiInterface.getPublicIpAddress();
        if(ip == null){
            return null;
        }

        Configuration.LOCATION_PUBLIC_IP = ip;

        LocationApi _client = new LocationApi();
        LocationData data = _client.getLocationDataByIpAddress(Configuration.LOCATION_PUBLIC_IP);
        if(data == null){
            return null;
        }
        Configuration.LOCATION_LONGITUDE = data.getLongitude()+"";
        Configuration.LOCATION_LATITUDE = data.getLatitude()+"";
        return String.format(format,data.getLatitude(),data.getLongitude());
    }

    private String getGridPointsEndPoint(String endPoint)throws IOException{
        //https://api.weather.gov/points/41.572,-93.7453
        String response = client.makeRequest(endPoint);
        JSONObject json = new JSONObject(response);
        JSONObject properties = json.getJSONObject("properties");
        return properties.getString("forecastHourly");
    }

    private WeatherData getBadData(){
        WeatherData wd = new WeatherData();
        wd.setWeather("Weather?");
        wd.setTemperature(0);
        wd.setWindSpeed("?");
        wd.setWindDirection("?");
        wd.setGoodData(false);

        return wd;
    }

    public static void main(String[] args){
        WeatherAPI api = new WeatherAPI();
        System.out.println(api.getWeatherNow().getTemperature());
    }
}
