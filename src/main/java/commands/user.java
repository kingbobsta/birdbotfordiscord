package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;


public class user extends Command {

    public user(){
        super.name = "user";
        super.aliases = new String[]{"u", "userinfo"};
        super.help = "gets information about a user";
        super.category = new Category("Information Commands");
        super.cooldown = 5;
        super.arguments = "[name as mention]";
    }


    @Override
    protected void execute(CommandEvent event){
        Member name;
        java.util.Date messageDate = new java.util.Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EE, MMM d, yyyy 'at' HH:mm z");
        if(event.getArgs().isEmpty()){
            name = event.getMember();
        }
        else {
            try {
                name = event.getMessage().getMentionedMembers().get(0);
            } catch (IndexOutOfBoundsException ex){
                event.reply("Please mention a user!");
                return;
            }
        }
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.red)
                    .setThumbnail(name.getUser().getAvatarUrl())
                    .setAuthor(name.getEffectiveName(),null,event.getGuild().getIconUrl())
                    .setDescription(name.getUser().getName() + " joined on " + formatter.format(Date.from(name.getTimeJoined().toInstant())))
                    .addField("Server Nickname: ", name.getNickname() == null ? "No Nickname Set" : name.getNickname(), true)
                    .addField("Current Activity: ", displayGameInfo(name), true)
                    .addField("Online Status: ", name.getOnlineStatus().toString(), true)
                    .addField("Roles: ", getRolesString(name.getRoles()), true)
                    .setFooter("Message Created On : " + formatter.format(messageDate),event.getGuild().getIconUrl());
                    event.reply(builder.build());
    }
    private String displayGameInfo(Member name){
        String status;
        try{
            status = name.getActivities().get(0).getName();
        } catch (IndexOutOfBoundsException ixx){
            status = "No Activity Set";
        }
        return status;
    }
    private String getRolesString(List rolesList){
        String roles = "";
        if(!rolesList.isEmpty()){
            Role tempRole = (Role) rolesList.get(0);
            roles = tempRole.getName();
            for(int i = 1; i < rolesList.size(); i++){
                tempRole = (Role) rolesList.get(i);
                roles = roles + ", " + tempRole.getName();
            }
        } else{
            roles = "No Roles Assigned";
        }

        return roles;
    }
}
