package thirdparty.weather;

import org.json.JSONArray;
import org.json.JSONObject;
import raspberryPi.Printer;
import raspberryPi.RPiInterface;
import thirdparty.ApiClient;
import thirdparty.location.LocationApi;
import thirdparty.location.LocationData;
import config.Configuration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WeatherAPI {

    ApiClient client = new ApiClient("https://api.weather.gov/");

    public WeatherData fakeIt(){
        WeatherData data = new WeatherData();
        data.setWeather("Cloudy");
        data.setTemperature(27);
        data.setIconURL("https://api.weather.gov/icons/land/night/sct?size=small");
        return data;
    }

    public List<WeatherData> getBadWeatherForecastDays(int days){
        List<WeatherData> list = new ArrayList<>();
        for(int i = 0; i < days; i++){
            list.add(getBadData());
        }
        return list;
    }

    public List<WeatherData> getWeatherForecastDays(int days){
        Printer.println("Requesting the weather from our beloved overlords.");

        try {
            List<WeatherData> list = new ArrayList<>();
            String endPoint;

            endPoint = getCoordinatesEndpoint();
            if(endPoint==null) {
                throw new IOException("No internet connection.");
            }
            endPoint = getGridPointsEndPoint(endPoint);//.replaceAll("hourly","");

            String response = client.makeRequest(endPoint);
            JSONObject json = new JSONObject(response);
            JSONObject properties = json.getJSONObject("properties");
            JSONArray periods = properties.getJSONArray("periods");


            return hoursToDays(periods,days);
        }catch (IOException e){
            List<WeatherData> list = new ArrayList<>();
            for(int i = 0; i < days; i++)
                list.add(getBadData());
            return list;
        }
    }

    public WeatherData getWeatherNow(){
        Printer.println("Requesting the weather from our beloved overlords.");

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

            WeatherData wd = createData(now);

            return wd;
        }catch (IOException e){
            return getBadData();
        }
    }

    private WeatherData createData(JSONObject object){
        WeatherData wd = new WeatherData();
        wd.setName(object.getString("name"));
        wd.setWeather(object.getString("shortForecast"));
        wd.setTemperature(object.getInt("temperature"));
        wd.setWindSpeed(object.getString("windSpeed"));
        wd.setDateTime(object.getString("startTime"));
        wd.setWindDirection(object.getString("windDirection"));
        wd.setIconURL(object.getString("icon").replaceAll("size=small","size=large"));

        return wd;
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
        wd.setName("???");
        wd.setWeather("Weather?");
        wd.setTemperature(0);
        wd.setWindSpeed("?");
        wd.setWindDirection("?");
        wd.setGoodData(false);

        return wd;
    }

    private List<WeatherData> hoursToDays(JSONArray dataList, int days){
        List<WeatherData> list = new ArrayList<>();
        HashMap<Integer, List<WeatherData>> map = new HashMap<>();

        int d = 0;
        for(int i = 0; i < dataList.length() && d < days; i++){
            WeatherData data = createData(dataList.getJSONObject(i));
            if(!map.containsKey(d)){
                List<WeatherData> wdList = new ArrayList<>();
                wdList.add(data);
                map.put(d,wdList);
            }else{
                List<WeatherData> today = map.get(d);
                if(sameDay(today.get(0),data)){
                    today.add(data);
                }else {
                    d++;
                    if(d >= days){
                        break;
                    }
                    List<WeatherData> wdList = new ArrayList<>();
                    wdList.add(data);
                    map.put(d,wdList);
                }
            }
        }

        Iterator<Integer> iter = map.keySet().iterator();
        while(iter.hasNext()){
            int key = iter.next();
            List<WeatherData> data = map.get(key);
            int temp = 0;
            int wind = 0;
            WeatherData today = data.get(0);
            list.add(today);
            for(WeatherData wd: data){
                temp += wd.getTemperature();
                wind += Integer.parseInt(wd.getWindSpeed().replaceAll("\\s+mph",""));
            }
            today.setTemperature(temp/data.size());
            today.setWindSpeed((wind/data.size())+"");
            today.setName(today.getDayOfWeek());
        }

        return list;
    }



    private boolean sameDay(WeatherData wd1, WeatherData wd2){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        int w1 = Integer.parseInt(dateFormat.format(wd1.getDate()));
        int w2 = Integer.parseInt(dateFormat.format(wd2.getDate()));

        return w1 == w2;
    }

    public static void main(String[] args){
        WeatherAPI api = new WeatherAPI();
        System.out.println(api.getWeatherNow().getTemperature());
    }
}
