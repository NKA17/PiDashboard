package thirdparty.discord;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class DiscordHub {
    //https://discordapp.com/developers/applications
    public static JDA jda = null;

    public static DiscordMessage message = new DiscordMessage();

    public static void start(){
        try {
            System.setProperty("http.agent", "Chrome");
            JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(Configuration.TOKEN);
            jda = builder.build();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
