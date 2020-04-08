package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class inviteToServer extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        //splits up the message
        String[] messageSent = event.getMessage().getContentRaw().split(" ");

        //basically just a bunch of commands and aliases and what they do
        if (messageSent[0].equalsIgnoreCase("%invite") && messageSent.length == 1) {
            event.getChannel().sendMessage("Here's an invite link to the server!").queue();
            event.getChannel().sendMessage(event.getChannel().createInvite().setMaxAge(86400).complete().getUrl()).queue();
            event.getChannel().sendMessage("This invite link lasts for 24 hours.").queue();
        }else if(messageSent[0].equalsIgnoreCase("%invite") && messageSent[1].equalsIgnoreCase("help")){
            event.getChannel().sendMessage("To use %invite, please use %invite").queue();
        } else if(messageSent.length >= 2 && messageSent[0].equalsIgnoreCase("%invite") && messageSent[1].equalsIgnoreCase("create")){
            event.getChannel().sendMessage("Here's an invite link to the server!").queue();
            event.getChannel().sendMessage(event.getChannel().createInvite().setMaxAge(86400).complete().getUrl()).queue();
            event.getChannel().sendMessage("This invite link lasts for 24 hours.").queue();
        }
    }
}
