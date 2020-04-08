package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

//THIS ONLY WILL WORK ON COMPUTER VERSIONS OF DISCORD BECAUSE OF MENTIONS
public class StrigSpam extends ListenerAdapter{

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        try {
            String name = event.getMember().getEffectiveName();
            for (int i = 0; i < event.getMessage().getMentionedMembers().size(); i++) {
                if (event.getMessage().getMentionedMembers().get(i).getId().equals("309103994062635019")) {
                    event.getChannel().sendMessage(":tada::tada::tada::tada: THE STRIG HAS BEEN SUMMONED BY " + name.toUpperCase() + " :tada::tada::tada::tada:").queue();
                    break;
                }
            }
        } catch (IndexOutOfBoundsException ex){
        }
    }
}
