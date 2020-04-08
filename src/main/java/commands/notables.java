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
import java.util.Arrays;

public class notables extends Command {

    public notables(){
        super.name = "notable";
        super.aliases = new String[]{"note", "not", "nots", "notes","notables","n","r", "rare"};
        super.arguments = "[Country Code (2 Letters)] [State Code (2 Letters)] [County Name]";
        super.help = "gets notable birds recently reported a region";
        super.category = new Category("eBird API Integration");
    }

    @Override
    protected void execute(CommandEvent event) {

        String[] args = event.getArgs().split(" ");
        String locCode;
        int results = 25;

        if(args.length < 1){
            event.reply("This command requires at least 2 arguments, please use %help for additional help");
            return;
        } else if(args.length >= 1){
            event.reply("Getting recent notable sightings!");
            String countryCode = args[0].toUpperCase();
            locCode = countryCode;
            if(args.length >= 2){
                String stateCode = args[1].toUpperCase();
                locCode = countryCode + "-" + stateCode;
                if(args.length >= 3){
                    String subregionName = "";
                    for(int i = 2; i < args.length; i++){
                        subregionName += args[i] + " ";
                    }
                    subregionName = subregionName.substring(0, subregionName.length()-1);
                    String urlThing = "https://api.ebird.org/v2/ref/region/list/subnational2/" + locCode + ".json";
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(urlThing)
                                .method("GET", null)
                                .addHeader("X-eBirdApiToken", "idnbs3hmk5lf")
                                .build();
                        Response res = client.newCall(request).execute();
                        if (!res.isSuccessful()){
                            event.replyError("Couldn't fetch data, check to see if your location is spelled correctly (`Format: [2 Letter Country Code] [2 Letter State/Province Code] [Subregion Name]`). If it's correct, then eBird's servers are possibly down.");
                            throw new RuntimeException("Couldn't fetch data, check to see if your location is spelled correctly ([2 Letter Country Code] [2 Letter State/Province Code] [Subregion Name]. If it's correct, then eBird's servers are possibly down.");
                        }
                        ResponseBody body = res.body();
                        String obsString = body.string();
                        JSONArray countyArray = new JSONArray(obsString);
                        for (int i = 0; i < countyArray.length(); i++) {
                            JSONObject json = countyArray.getJSONObject(i);
                            String countName = json.getString("name");
                            if(countName.equalsIgnoreCase(subregionName)){
                                String countyCode = json.getString("code");
                                locCode = countyCode;
                                break;
                            }
                        }
                    } catch(IOException | RuntimeException e){
                        event.replyError("Error: couldn't get data (possibly too much data for the bot to handle).");
                    }
                }
            }
            String urlToUse = "https://api.ebird.org/v2/data/obs/" + locCode + "/recent/notable?maxResults=" + (results+1);
            if (results > 25 || results < 25) {
                event.reply("Error: results should be 25. This is a Drew problem.");
            } else {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlToUse)
                            .method("GET", null)
                            .addHeader("X-eBirdApiToken", "idnbs3hmk5lf")
                            .build();
                    Response res = client.newCall(request).execute();
                    if (!res.isSuccessful()){
                        event.replyError("Couldn't fetch data, check to see if your location is spelled correctly (`Format: [2 Letter Country Code] [2 Letter State/Province Code] [Subregion Name]`). If it's correct, then eBird's servers are possibly down.");
                        throw new RuntimeException("Couldn't fetch data, check to see if location is spelled correctly. If it's correct, then eBird's servers are possibly down.");
                    }
                    ResponseBody body = res.body();
                    String obsString = body.string();
                    JSONArray observationArray = new JSONArray(obsString);
                    String[] messageContent = new String[observationArray.length()];
                    if(messageContent.length > results + 1){
                        String[] checker = new String[results + 1];
                        messageContent = checker;
                    }
                    messageContent[0] = "**THE " + (messageContent.length-1) + " MOST RECENT NOTABLE OBSERVATIONS FOR " + locCode + "**";
                    for (int i = 1; i < messageContent.length; i++) {
                        JSONObject json = observationArray.getJSONObject(i-1);
                        String comName = json.getString("comName");
                        String location = json.getString("locName");
                        String obsDate = json.getString("obsDt");
                        boolean validity = json.getBoolean("obsValid");
                        boolean reviewed = json.getBoolean("obsReviewed");
                        String howMany;
                        String rev;
                        try {
                            howMany = String.valueOf(json.getLong("howMany"));
                        }
                        catch (JSONException e) {
                            howMany = "X";
                        }
                        String yeehaw;
                        if(validity)
                            yeehaw = "CONFIRMED";
                        else
                            yeehaw = "UNCONFIRMED";
                        if(reviewed)
                            rev = "Reviewed";
                        else
                            rev = "Not Reviewed";
                        String individual = "\n***" + comName + "*** (" + howMany + ") *at " + location + "* on " + obsDate + " (" + yeehaw + ", " + rev + ")";
                        messageContent[i] = individual;
                    }
                    String finalMessage = Arrays.toString(messageContent);
                    finalMessage = finalMessage.substring(1, finalMessage.length() - 1);
                    event.reply(finalMessage);
                } catch(IOException | RuntimeException e){
                    event.reply(e.toString());
                    event.replyError("No notable sightings for region specified. Check for any other error messages. If you think this is inaccurate, please tell @drew#3549.");
                    System.out.println(e);
                }
            }
        }
    }
}

