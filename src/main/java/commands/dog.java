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

public class dog extends Command {

    public dog(){
        //sets name, aliases, help info etc.
        super.name = "dog";
        super.aliases = new String[]{"randdog", "randog", "rdog", "randomdog"};
        super.help = "generates dog pictures using dog.ceo API";
        super.category = new Category("Random");
    }

    @Override
    protected void execute(CommandEvent event) { ;

        //sets the color for the Embed
        Color color;
        if (event.isFromType(ChannelType.PRIVATE))
            color = Color.decode("#33ff00");
        else
            color = event.getMember().getColor();

        //splits up the message
        String[] message = event.getMessage().getContentRaw().split(" ");

        //will fetch random dog image if one word specified
        if(message.length == 1) {
            try {
                //builds embed
                EmbedBuilder builder = new EmbedBuilder();

                //builds client, requests, gets response
                OkHttpClient client = new OkHttpClient();
                Request req = new Request.Builder().url("https://dog.ceo/api/breeds/image/random").get().build();
                Response res = client.newCall(req).execute();

                //error message if it fails
                if(!res.isSuccessful()) {
                    event.reply("Couldn't reach webpage. Perhaps servers are down.");
                    throw new RuntimeException("Couldn't reach webpage.");
                }

                //converts response into a string
                ResponseBody body = res.body();
                String data = body.string();
                JSONObject json = new JSONObject(data);
                String dog = json.getString("message");

                //builds embed and sends it
                builder.setAuthor("Requested by " + event.getAuthor().getName(), null, event.getAuthor().getEffectiveAvatarUrl());
                builder.setImage(dog);
                builder.setFooter("Image provided by the dog.ceo API", event.getGuild().getIconUrl());
                builder.setColor(color);
                event.reply(builder.build());

            //catches if theres something big bad
            } catch (IOException | RuntimeException e) {
                event.replyError("Drew probably screwed something up.");
                event.reply(e.toString());
                System.out.println(e);
            }
        }

        //fetches breed of dog if 1 arg provided
        if(message.length == 2) {
            //gets breed, puts it into the URL, fetches random of breed
            String breed = message[1].toLowerCase();
            String url = "https://dog.ceo/api/breed/" + breed + "/images/random";

            //literally the same thing as above
            try {
                EmbedBuilder builder = new EmbedBuilder();
                OkHttpClient client = new OkHttpClient();
                Request req = new Request.Builder().url(url).get().build();
                Response res = client.newCall(req).execute();
                if (!res.isSuccessful())
                    throw new RuntimeException("Error while fetching remote resource");
                ResponseBody body = res.body();
                String data = body.string();
                JSONObject json = new JSONObject(data);
                String dog = json.getString("message");

                builder.setAuthor("Requested by " + event.getAuthor().getName(), null, event.getAuthor().getEffectiveAvatarUrl());
                builder.setImage(dog);
                builder.setFooter("Image provided by the dog.ceo API", event.getGuild().getIconUrl());
                builder.setColor(color);
                event.reply(builder.build());
            } catch (IOException | RuntimeException e) {
                event.replyError("Either that dog doesn't exist in the API or Drew screwed something up.");
                event.reply(e.toString());
                System.out.println(e);
            }
        }

        //if breed and subbreed are provided then this happens
        if(message.length == 3){
            //gets dog breed and subbreed, sticks it into the URL
            String type = message[2].toLowerCase();
            String subbreed = message[1].toLowerCase();
            String urlTwo = "https://dog.ceo/api/breed/" + type + "/" + subbreed + "/images/random";

            //literally the same stuff as above
            try {
                EmbedBuilder builder = new EmbedBuilder();
                OkHttpClient client = new OkHttpClient();
                Request req = new Request.Builder().url(urlTwo).get().build();
                Response res = client.newCall(req).execute();
                if (!res.isSuccessful())
                    throw new RuntimeException("Error while fetching remote resource");
                    ResponseBody body = res.body();
                    String data = body.string();
                    JSONObject json = new JSONObject(data);
                    String dog = json.getString("message");

                    builder.setAuthor("Requested by " + event.getAuthor().getName(), null, event.getAuthor().getEffectiveAvatarUrl());
                    builder.setImage(dog);
                    builder.setFooter("Image provided by the dog.ceo API", event.getGuild().getIconUrl());
                    builder.setColor(color);
                    event.reply(builder.build());
            } catch (IOException | RuntimeException e) {
                    event.replyError("Either that dog doesn't exist in the API or Drew screwed something up.");
            }
        }
    }
}

