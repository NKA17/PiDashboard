package config;

import thirdparty.location.LocationData;

import java.awt.*;

public class Configuration {
    public static String MODE = "default";

    public static boolean DISPLAY_SHOW_CURSOR = true;

    public static boolean LOGGING = false;
    public static String LOGGING_PATH = "";
    public static String LOGGING_FILE_FORMAT = "log";

    public static boolean CONNTECTED_TO_INTERNET = true;
    public static boolean CHECK_INTERNET = false;

    public static boolean PRINT = false;
    public static String LOCATION_PUBLIC_IP = null;
    public static String LOCATION_LOCAL_IP = null;
    public static String LOCATION_LONGITUDE = null;
    public static String LOCATION_LATITUDE = null;
    public static LocationData LOCATION_DATA = null;

    public static int SLEEP_AFTER_TIME = 40000;
    public static boolean WAKE = true;
    public static int SWAP_TIME = 30000;
    public static String SCRIPT_LOCATION = "";
    public static String IMAGE_LOCATION = "";
    public static int WIDTH_CONSTRAINT = -1;
    public static int HEIGHT_CONSTRAINT = -1;

    public static Insets GRIDBAG_INSETS = new Insets(4,4,4,10);

    public static Color WINDOW_BG_COLOR = new Color(30,20,40);
    public static Color PANEL_BG_COLOR = new Color(60,60,80);
    public static Color SCREEN_BG_COLOR = new Color(60,60,80);

    public static Color TEXT_COLOR = new Color(220,220,220);
    public static final Font TEXT_FONT = new Font("Yu Gothic UI",Font.BOLD,20);

    public static boolean CLOCK_MERIDIAN = false;
    public static boolean CLOCK_MILITARY = false;

}
