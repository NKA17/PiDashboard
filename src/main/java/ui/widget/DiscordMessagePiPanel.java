package ui.widget;

import config.Configuration;
import thirdparty.discord.DiscordHub;
import thirdparty.discord.DiscordMessage;
import ui.tools.font.FontInfo;
import ui.tools.font.FontTool;
import ui.view.PiPanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DiscordMessagePiPanel extends PiPanel{

    public DiscordMessagePiPanel(){
        super(Configuration.WIDTH_CONSTRAINT,Configuration.HEIGHT_CONSTRAINT);
    }

    @Override
    public void draw(Graphics g) {
        int L = Configuration.GRIDBAG_INSETS.left;
        int R = Configuration.GRIDBAG_INSETS.right;
        int T = Configuration.GRIDBAG_INSETS.top;
        int B = Configuration.GRIDBAG_INSETS.bottom;

        DiscordMessage message = DiscordHub.message;

        int x = getX()+L;
        int y = getY()+T;
        int titleHeight = 30;
        int messageHeight = 15;

        Font title = new Font(getFont().getFontName(),Font.BOLD,getFont().getSize());
        FontInfo titleFontInfo = FontTool.fitInBox(title,message.getGuildName(),getW()-L-R,titleHeight);
        g.setFont(titleFontInfo.getFont());
        g.setColor(Configuration.TEXT_COLOR);
        FontTool.drawAAString(g,message.getGuildName(),x,y);
        y+=titleHeight+B;

        BufferedImage pfp = message.getProfilePic((int)(getH()*.66),(int)(getH()*.66));
        g.drawImage(pfp,x,y+T,null);
        x+=pfp.getWidth()+R;

        FontInfo usernameInfo = FontTool.fitInBox(title,message.getUsername(),getW()-L-R-pfp.getWidth(),titleHeight);
        g.setFont(usernameInfo.getFont());
        FontTool.drawAAString(g,message.getUsername(),x+L,y+T);
        y+=titleHeight+B+T;

        Rectangle rect = new Rectangle(x,y,getW()-L-R-pfp.getWidth(),getH()-(y-getY()));

        g.setColor(Configuration.WINDOW_BG_COLOR);
        g.fillRect(rect.x,rect.y+T,rect.width,rect.height);

        FontInfo messageFontInfo = FontTool.fitInBox(title, "Tq", rect.width-L-R, messageHeight);
        g.setFont(messageFontInfo.getFont());
        g.setColor(Configuration.TEXT_COLOR);
        FontTool.drawAAStringInBox(g,message.getMessage(),rect.x+L, rect.y+T, rect.width-L-R,rect.height-T-B);
    }

    @Override
    public void update() {
        PiPanel.addToUpdateQueue(this);
    }

    @Override
    public boolean isPowered() {
        return false;
    }
}
