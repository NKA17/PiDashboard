package thirdparty.location;

public class LocationData {

//    "ip": "173.19.141.95",
    private String ip;
//    "city": "Urbandale",
    private String city;
//    "region": "Iowa",
    private String region;
//    "region_code": "IA",
    private String region_code;
//    "country": "US",
    private String country;
//    "country_code": "US",
    private String country_code;
//    "country_code_iso3": "USA",
//    "country_capital": "Washington",
//    "country_tld": ".us",
//    "country_name": "United States",
    private String countryName;
//    "continent_code": "NA",
//    "in_eu": false,
//    "postal": "50322",
//    "latitude": 41.6289,
    private double latitude;
//    "longitude": -93.7255,
    private double longitude;
//    "timezone": "America/Chicago",
//    "utc_offset": "-0600",
//    "country_calling_code": "+1",
//    "currency": "USD",
//    "currency_name": "Dollar",
//    "languages": "en-US,es-US,haw,fr",
//    "country_area": 9629091.0,
//    "country_population": 310232863.0,
//    "asn": "AS30036",
//    "org": "MEDIACOM-ENTERPRISE-BUSINESS"
//


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
