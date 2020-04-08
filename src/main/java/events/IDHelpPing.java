package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class IDHelpPing extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if(event.getAuthor().isBot()) return;
        String channelName = event.getChannel().getName();
        String[] messageSent = event.getMessage().getContentRaw().split(" ");

        if(!event.getMember().getUser().isBot()) {
            if(channelName.equals("id-help-and-quizzes")){
                for(int i = 0; i < messageSent.length; i++){
                    if(messageSent[i].equalsIgnoreCase("help")) {
                        event.getChannel().sendMessage("<@&492099532197462026>").queue();
                    }
                }
            }
        }
    }
}
