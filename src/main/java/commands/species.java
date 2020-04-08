package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;

public class species extends Command {

    public species(){
        super.name = "species";
        super.aliases = new String[]{"s","sp"};
        super.help = "fetches information on a species with a common name";
        super.arguments = "[species name]";
        super.category = new Category("eBird API Integration");
    }

    @Override
    protected void execute(CommandEvent event){
        String[] args = event.getArgs().split(" ");
        String speciesSearch = "";
        for(int p = 0; p < args.length; p++){
            speciesSearch += args[p] + " ";
        }
        speciesSearch = speciesSearch.substring(0,speciesSearch.length()-1);

        String urlToUse = "https://api.ebird.org/v2/ref/taxonomy/ebird?fmt=json";
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urlToUse)
                    .method("GET", null)
                    .addHeader("X-eBirdApiToken", "{{eBird API TOKEN}}")
                    .build();
            Response res = client.newCall(request).execute();
            if (!res.isSuccessful()) {
                event.replyError("Couldn't fetch data, check to see if your location is spelled correctly (`Format: [2 Letter Country Code] [2 Letter State/Province Code] [Subregion Name]`). If it's correct, then eBird's servers are possibly down.");
                throw new RuntimeException("Couldn't fetch data, check to see if location is spelled correctly. If it's correct, then eBird's servers are possibly down.");
            }
            ResponseBody body = res.body();
            String taxFullString = body.string();
            JSONArray tax = new JSONArray(taxFullString);
            for(int i = 0; i < tax.length(); i++) {
                try{
                    JSONObject json = tax.getJSONObject(i);
                    String comName = json.getString("comName");
                    if(comName.equalsIgnoreCase(speciesSearch)){
                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Color.green)
                                .setTitle("Species Profile: " + json.getString("comName"))
                                .addField("**Common Name**",json.getString("comName"),true)
                                .addField("**Scientific Name**",json.getString("sciName"),false)
                                .addField("**Banding Code**", bandCode(json) ,true)
                                .addField("**Order**",json.getString("order"),true)
                                .addField("**Family**",json.getString("familyComName") + "/" + json.getString("familySciName"),true)
                                .addField("**eBird Species Page**", "https://ebird.org/species/" + json.getString("speciesCode"), false);
                        event.reply(builder.build());
                    }
                } catch (JSONException e){
                    System.out.println("lol ur bad");
                }
            }
        } catch (IOException | RuntimeException e){
            event.reply(e.toString());
            System.out.println(e);
        }
    }

    private String bandCode(JSONObject j){
        String code;
        try{
            JSONArray codeGrab = j.getJSONArray("bandingCodes");
            code = codeGrab.get(0).toString();
        } catch (JSONException e){
            code = "None";
        }
        return code;
    }
}
