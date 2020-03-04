package thirdparty.location;

import org.json.JSONObject;
import raspberryPi.RPiInterface;
import thirdparty.ApiClient;
import config.Configuration;

import java.io.IOException;

public class LocationApi {
    //https://ipapi.co/173.19.141.95/json/
    ApiClient client = new ApiClient("https://ipapi.co/");

    public LocationData getLocationDataByIpAddress(String ipAddress){
        try {
            String response = client.makeRequest(ipAddress + "/json");

            JSONObject json = new JSONObject(response);

            LocationData data = new LocationData();
            data.setIp(json.getString("ip"));
            data.setCity(json.getString("city"));
            data.setRegion(json.getString("region"));
            data.setRegion_code(json.getString("region_code"));
            data.setCountry_code(json.getString("country_code"));
            data.setCountry(json.getString("country"));
            data.setCountryName(json.getString("country_name"));
            data.setLatitude(json.getDouble("latitude"));
            data.setLongitude(json.getDouble("longitude"));

            return data;
        }catch (IOException e){
            return null;
        }
    }

    public boolean configureLocation(){
        if(Configuration.LOCATION_PUBLIC_IP == null){
            RPiInterface.getPublicIpAddress();
        }
        if(Configuration.LOCATION_PUBLIC_IP == null){
            return false;
        }
        LocationData data = getLocationDataByIpAddress(Configuration.LOCATION_PUBLIC_IP);
        if(data == null){
            return false;
        }

        Configuration.LOCATION_LONGITUDE = data.getLongitude()+"";
        Configuration.LOCATION_LATITUDE = data.getLatitude()+"";
        Configuration.LOCATION_DATA = data;

        return true;

    }
}
