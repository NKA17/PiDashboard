package thirdparty;

import raspberryPi.RPiInterface;
import ui.config.Configuration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiClient {


    private String url;

    public ApiClient(String base) {
        this.url = base;
    }

    private String cleanURL(String endPoint){
        String requestUrl = url + endPoint;

        //incase they doubled on the http. For some reason I'm making that my problem
        requestUrl = requestUrl.replaceAll(String.format("(%s)+",url),url);

        String http = requestUrl.split(":")[0];

        //cleans up the double forward slashes
        requestUrl = http+"://"+requestUrl
                .replaceAll("http(s)?://","")
                .replaceAll("//","/");

        return requestUrl;
    }

    public BufferedImage getImage(String path) throws IOException{
        try {
            if(Configuration.CHECK_INTERNET){
                boolean connected = RPiInterface.checkInternetConnection();
                if(!connected){
                    throw new IOException("No internet connection.");
                }
            }
            path = cleanURL(path);
            URL url = new URL(path);
            BufferedImage img = ImageIO.read(url);
            return img;
        }catch (IOException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String makeRequest(String endPoint) throws IOException{
        try{
            if(Configuration.CHECK_INTERNET){
                boolean connected = RPiInterface.checkInternetConnection();
                if(!connected){
                    throw new IOException("No internet connection.");
                }
            }

            String requestUrl = cleanURL(endPoint);


            HttpURLConnection conn = (HttpURLConnection) (new URL(requestUrl)).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept","application/json");


            if (conn.getResponseCode() != 1000 && conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            String jsonstr = "";
            while ((output = br.readLine()) != null) {
                jsonstr += output;
            }

            conn.disconnect();
            return jsonstr;

        }catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {
            throw e;
        }
        return "{}";
    }
}
