package thirdparty.discord;

import graphics.ImageCache;
import ui.tools.graphics.ImageTransform;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class DiscordMessage {
    private BufferedImage profilePic = null;
    private String username;
    private String message;
    private String guildName;
    private String channelName;
    private String pfpURL;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public BufferedImage getProfilePic() {
        try {
            BufferedImage pfp = ImageCache.get(new URL(pfpURL));

            BufferedImage image = new BufferedImage(pfp.getWidth(),pfp.getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2 = image.createGraphics();
            g2.setComposite(AlphaComposite.Src);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillOval(0,0,image.getWidth(),image.getHeight());

            g2.setComposite(AlphaComposite.SrcAtop);
            g2.drawImage(pfp, 0, 0, null);

            g2.dispose();

            return image;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage getProfilePic(int w, int h){
        BufferedImage image = getProfilePic();
        image = ImageTransform.resize(image,w,h);
        profilePic = image;
        return image;
    }

    public void setProfilePic(BufferedImage profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public String getPfpURL() {
        return pfpURL;
    }

    public void setPfpURL(String pfpURL) {
        this.pfpURL = pfpURL;
    }
}
