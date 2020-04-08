package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class code extends Command {

    public code(){
        super.name = "code";
        super.aliases = new String[]{"c","co"};
        super.help = "fetches a banding code for species specified";
        super.arguments = "[species name]";
        super.category = new Category("eBird API Integration");
    }

    @Override
    protected void execute(CommandEvent event){
        String[] args = event.getArgs().split(" ");
        if(args.length == 0){
            event.reply("Command requires a bird's common name.");
            return;
        }
        event.reply("Fetching banding code!");
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
                    .addHeader("X-eBirdApiToken", "idnbs3hmk5lf")
                    .build();
            Response res = client.newCall(request).execute();
            if (!res.isSuccessful()) {
                event.replyError("Couldn't fetch data. eBird's servers are possibly down.");
                throw new RuntimeException("eBird's servers are possibly down.");
            }
            ResponseBody body = res.body();
            String taxFullString = body.string();
            JSONArray tax = new JSONArray(taxFullString);
            for(int i = 0; i < tax.length(); i++) {
                try{
                    JSONObject json = tax.getJSONObject(i);
                    String comName = json.getString("comName");
                    if(comName.equalsIgnoreCase(speciesSearch)){
                        try{
                            JSONArray codeGrab = json.getJSONArray("bandingCodes");
                            String code = codeGrab.get(0).toString();
                            event.reply("The banding code for " + comName + " is: *" + code + "*");
                            return;
                        } catch (JSONException e){
                            event.reply("Species has no code specified in eBird's taxa database.");
                            return;
                        }
                    }
                } catch (JSONException e){
                }
            }
        } catch (IOException | RuntimeException e){
            event.reply("Please let Drew know of the following error: ");
            event.reply(e.toString());
            System.out.println(e);
            return;
        }
        event.reply("Could not match common name within eBird's taxa. Please check your spelling of the bird's name.");
    }
}
