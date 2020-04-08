package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;

public class cat extends Command {

    public cat(){
        //sets name, aliases, help info etc.
        super.name = "cat";
        super.aliases = new String[]{"randcat", "rancat", "rcat", "randomcat"};
        super.help = "generates a random dog picture using random.cat API";
        super.category = new Category("Random");
    }

    @Override
    protected void execute(CommandEvent event) {
        //sets the color for the Embed
        Color color;
        if (event.isFromType(ChannelType.PRIVATE))
            color = Color.decode("#33ff00");
        else
            color = event.getMember().getColor();

        //try this! it should work! right???
        try {

            //creates the embed builder
            EmbedBuilder builder = new EmbedBuilder();

            //creates the API grabber, grabs it, puts it into a response
            OkHttpClient client = new OkHttpClient();
            Request req = new Request.Builder().url("http://aws.random.cat/meow").get().build();
            Response res = client.newCall(req).execute();

            //error message if couldn't reach
            if(!res.isSuccessful()) {
                event.reply("Couldn't reach webpage. Perhaps servers are down.");
                throw new RuntimeException("Couldn't reach webpage.");
            }

            //converts response to a string
            ResponseBody body = res.body();
            String data = body.string();
            JSONObject json = new JSONObject(data);
            String cat = json.getString("file");

            //builds the embed, sends it
            builder.setAuthor("Requested by "+event.getAuthor().getName(), null, event.getAuthor().getEffectiveAvatarUrl());
            builder.setImage(cat);
            builder.setFooter("Image provided by random.dog API", event.getGuild().getIconUrl());
            builder.setColor(color);
            event.reply(builder.build());
        }

        //if somehow something gets really screwed up with this, this catches it
        catch(IOException | RuntimeException e)
        {
            event.replyError("Drew probably screwed something up.");
            event.reply(e.toString());
            System.out.println(e);
        }
    }
}
