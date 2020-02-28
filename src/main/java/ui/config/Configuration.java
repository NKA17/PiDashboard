package ui.config;

import java.awt.*;

public class Configuration {
    public static int WAKE_SCREEN_TIME = 40000;
    public static int SWAP_TIME = 30000;
    public static String SCRIPT_LOCATION = "NOT SUPPLIED!";
    public static int WIDTH_CONSTRAINT = -1;
    public static int HEIGHT_CONSTRAINT = -1;

    public static Insets GRIDBAG_INSETS = new Insets(4,4,4,10);

    public static Color WINDOW_BG_COLOR = new Color(30,20,40);
    public static Color PANEL_BG_COLOR = new Color(60,60,80);

    public static Color TEXT_COLOR = new Color(220,220,220);
    public static final Font TEXT_FONT = new Font("Yu Gothic UI",Font.BOLD,20);
}
