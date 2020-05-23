package run;


import config.ConfigParser;
import organization.ScreenOrganizer;
import raspberryPi.Printer;
import raspberryPi.RPiInterface;
import realTime.ImmediateAction;
import realTime.RefreshRateHandler;
import realTime.TimeUnit;
import thirdparty.discord.DiscordHandler;
import thirdparty.location.LocationApi;
import config.Configuration;
import config.Setup;
import ui.view.PiFrame;
import ui.view.PiPanel;
import ui.view.PiScreen;
import ui.widget.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Startup {
    //Host Machine Dependency [sudo [-H]] pip[2|3] install gps3
    //      https://pypi.org/project/gps3/

    public static void main(String[] args){
        Setup.setup(args);
        if(Configuration.MODE.equalsIgnoreCase("exec")){
            testExecMode();
        }

        Printer.println("---------------------------------------");
        Printer.println("Starting Dashboard...");
        Printer.println("---------------------------------------");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = 1;
        gbc.insets = Configuration.GRIDBAG_INSETS;

        PiFrame pf = new PiFrame();

        PiScreen clockScreen = pf.createScreen(pf.getContentPane().getWidth(),pf.getContentPane().getHeight());

        InternetStatusPanel isp = new InternetStatusPanel();
        isp.setRefreshInterval(10,TimeUnit.MINUTES);
        clockScreen.addHiddenPanel(isp);
        ImmediateAction goodAction = new ImmediateAction() {
            @Override
            public void action() {
                clockScreen.removeFooterPanel(isp);
                RPiInterface.getLocalIpAddress();
            }
        };
        ImmediateAction badAction = new ImmediateAction() {
            @Override
            public void action() {
                clockScreen.addFooterPanel(isp, 0);
            }
        };
        ImmediateAction extendedAction = new ImmediateAction() {
            @Override
            public void action() {
                if(!isp.getInternetStatus()){
                    RPiInterface.wakeScreen();
                }
            }
        };
        isp.setStatusTurnedGoodAction(goodAction);
        isp.setStatusTurnedBadAction(badAction);
        isp.setExtendedAction(extendedAction);

        ScreenOrganizer organizer = new ScreenOrganizer();
        clockScreen.setOrganizer(organizer);

        startClock(Setup.configParser,clockScreen);

        if(getLocationData()){
            TextPanel city = new TextPanel(Configuration.LOCATION_DATA.getCity()+", "+
                    Configuration.LOCATION_DATA.getRegion()+" "+
                    Configuration.LOCATION_DATA.getCountryName(),120,20);
            clockScreen.addFooterPanel(city);
        }

        startWeather(Setup.configParser,clockScreen);

        LocalIpDisplay localIP = new LocalIpDisplay(60,30);
        localIP.setFormat("(%s)");
        clockScreen.addFooterPanel(localIP);
        localIP.setRefreshInterval(12,TimeUnit.HOURS);

        pf.timer(100);

        if(!Configuration.DISPLAY_SHOW_CURSOR) {
            BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(0, 0), "blank cursor");
            pf.getContentPane().setCursor(blankCursor);
        }

        startTemp(Setup.configParser,clockScreen);

        startDiscord(Setup.configParser,clockScreen);

        pf.pack();
        EventQueue.invokeLater(() -> {
            pf.setVisible(true);
        });
    }

    private static boolean getLocationData(){
        LocationApi client = new LocationApi();
        return client.configureLocation();
    }

    private static void testExecMode(){
        try{
            System.out.println("$ "+Setup.configParser.getArg("--message"));
            Process p = Runtime.getRuntime().exec(Setup.configParser.getArg("--message"));
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            int i = 100;
            String out;
            while((out = reader.readLine()) != null && (i--) > 0){
                System.out.println(out);
            }

            if(p.isAlive()){
                p.destroy();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.exit(-1);
    }

    private static void startClock(ConfigParser cp, PiScreen screen){
        Clock clock = new Clock(screen.getW(),screen.getH());
        boolean added = addToScreen(cp,"clock",screen,clock);
        if(added){
            clock.setRefreshInterval(getRefreshInterval(cp,"clock", 200));
            String mode = cp.getArg("clock.mode");
            if(mode != null){
                mode = mode.toLowerCase();
                switch (mode){
                    case "military":
                        clock.useMilitaryTime();
                        break;
                    case "meridian":
                        clock.setUseMeridean(true);
                        break;
                    case "basic":
                        clock.setUseMeridean(false);
                        break;
                    default:
                        clock.setUseMeridean(true);
                }
            }
            clock.setUpdateRegex(cp.getArg("clock.updateRegex"));
        }
    }

    private static void startTemp(ConfigParser cp, PiScreen screen){
        PiTempPanel panel = new PiTempPanel();
        boolean added = addToScreen(cp,"temp",screen,panel);
        if(added){
            panel.setRefreshInterval(getRefreshInterval(cp,"temp",1000));
            panel.setHistorySize(cp.getInt("temp.history.size",30));
            panel.setWarningTrheshold(cp.getInt("temp.threshold.warning",70));
            panel.setWarningTrheshold(cp.getInt("temp.threshold.danger",80));
            panel.setWarningTrheshold(cp.getInt("temp.threshold.emergencyClose",84));
        }
    }

    private static void startWeather(ConfigParser cp, PiScreen screen){
        WeatherImage panel = new WeatherImage(150,60);
        boolean added = addToScreen(cp,"weather",screen,panel);
        if(added){
            panel.setRefreshInterval(getRefreshInterval(cp,"weather",600000));
        }
    }

    private static void startDiscord(ConfigParser cp, PiScreen screen){
        DiscordMessagePiPanel panel = new DiscordMessagePiPanel();
        boolean added = addToScreen(cp,"discord",screen,panel);
        if(added){
            thirdparty.discord.Configuration.BOT_ID = cp.getArg("discord.botId");
            thirdparty.discord.Configuration.TOKEN = cp.getArg("discord.token");
            DiscordHandler dh = new DiscordHandler();
            dh.setObserved(panel);
            dh.start();
        }
    }

    private static boolean addToScreen(ConfigParser cp, String module, PiScreen screen, PiPanel panel){
        String position = cp.getArg("module."+module);

        if(position == null){
            return false;
        }

        position = position.toLowerCase();

        switch (position){
            case "none":
                return false;
            case "footer":
                screen.addFooterPanel(panel);
                break;
            case "header":
                screen.addPiPanel(panel);
                break;
            case "hidden":
                screen.addHiddenPanel(panel);
                break;
        }

        Printer.println("Module added: '%s' (%s)",module,position);

        String isFocus = cp.getArg(module+".focused");
        if(isFocus != null && isFocus.toLowerCase().equals("true")){
            screen.getOrganizer().setMainPanel(panel);
        }

        return true;
    }

    private static long getRefreshInterval(ConfigParser cp, String module, long defaultValue){
        if(!cp.containsKey(module+".interval")){
            return defaultValue;
        }
        long interval = cp.getLong(module+".interval");
        String unit = cp.getArg(module+".interval.unit");

        return RefreshRateHandler.calculateInterval(interval,unit);
    }
}
