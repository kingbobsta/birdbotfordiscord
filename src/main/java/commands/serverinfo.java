package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class serverinfo extends Command {

    public serverinfo(){
        super.name = "serverinfo";
        super.aliases = new String[]{"server"};
        super.help = "gives server information.";
        super.category = new Category( "Information Commands");
    }

    @Override
    protected void execute(CommandEvent e){
        Date messageDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EE, MMM d, yyyy 'at' HH:mm z");
        Date serverCreated = Date.from(e.getGuild().getTimeCreated().toInstant());
        String members = Integer.toString(e.getGuild().getMembers().size());
        String channels = Integer.toString(e.getGuild().getChannels().size());
        String roles = Integer.toString(e.getGuild().getRoles().size());

        //builds the embed
        EmbedBuilder eB = new EmbedBuilder();
        eB.setColor(Color.red);
        eB.setAuthor(e.getGuild().getName(),null,e.getAuthor().getAvatarUrl());
        eB.setThumbnail(e.getGuild().getIconUrl());
        eB.addField("Server Owner: ",  "<@!" + e.getGuild().getOwnerId() + ">",false);
        eB.addField("Server Created On: ", formatter.format(serverCreated),false);
        eB.addField("Member Count: ", members, true);
        eB.addField("Channel Count: ", channels, true);
        eB.addField("Role Count: ", roles, true);
        eB.setFooter("Command sent on " + formatter.format(messageDate), e.getGuild().getIconUrl());
        e.getChannel().sendMessage(eB.build()).queue();

    }

}
