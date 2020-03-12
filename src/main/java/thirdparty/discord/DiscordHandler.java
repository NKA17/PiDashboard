package thirdparty.discord;

import jdk.nashorn.internal.parser.Token;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import ui.view.PiPanel;

public class DiscordHandler extends ListenerAdapter {

    private PiPanel observed = null;

    public PiPanel getObserved() {
        return observed;
    }

    public void setObserved(PiPanel observed) {
        this.observed = observed;
    }

    public void start(){
        if(DiscordHub.jda == null){
            DiscordHub.start();
        }

        DiscordHub.jda.addEventListener(this);
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if(!Configuration.BOT_ID.equalsIgnoreCase(event.getAuthor().getId())) {
            DiscordMessage message = new DiscordMessage();
            message.setPfpURL(event.getMessage().getAuthor().getAvatarUrl());
            message.setGuildName(event.getMessage().getGuild().getName());
            message.setChannelName(event.getMessage().getChannel().getName());
            message.setUsername(event.getMessage().getAuthor().getName());
            message.setMessage(event.getMessage().getContentRaw());

            DiscordHub.message = message;

            if(observed != null){
                observed.update();
            }
        }
    }
}
