import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import commands.*;
import events.HelloEvent;
import events.IDHelpPing;
import events.StrigSpam;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class Bot {

    public static void main(String args[]) throws Exception{

        String activity = "%help | being built";

        JDA jda = new JDABuilder("{BOT API KEY}").build();

        CommandClientBuilder cBuilder = new CommandClientBuilder();

        cBuilder.setActivity(Activity.playing(activity));

        //jda-util setup
        cBuilder.setOwnerId("692619256294015016");
        cBuilder.setPrefix("%");
        cBuilder.setHelpWord("help");

        //jda-util based commands

        //server info command
        cBuilder.addCommand(new serverinfo());

        //user
        cBuilder.addCommand(new user());

        //ebird recent observations pull
        cBuilder.addCommand(new recentobs());

        //ebird recent notables pull
        cBuilder.addCommand(new notables());

        //ebird recent hotspot
        cBuilder.addCommand(new hotspot());

        //ebird taxon bc
        cBuilder.addCommand(new bandcode());

        //ebird taxon species
        cBuilder.addCommand(new species());

        //ebird code
        cBuilder.addCommand(new code());

        //thumbnail builder
        cBuilder.addCommand(new thumbnailmaker());

        //calculator command
        cBuilder.addCommand(new calculator());

        //random dog
        cBuilder.addCommand(new dog());

        //random cat
        cBuilder.addCommand(new cat());

        //random bird fact
        cBuilder.addCommand(new birdfact());

        //poop
        cBuilder.addCommand(new birdpoopinurmouth());

        //making the command builder run

        CommandClient client = cBuilder.build();
        jda.addEventListener(client);

        //listener stuff

        //generate server invite
        jda.addEventListener(new inviteToServer());

        //responds whenever the word "hello" is said
        jda.addEventListener(new HelloEvent());


        //random listener features

        //pings the id helpers role in the bird herd whenever "help" is said
        jda.addEventListener(new IDHelpPing());

        //spams party emotes when strigidae is mentioned in the bird herd server
        jda.addEventListener(new StrigSpam());

    }
}
