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
import java.util.*;

public class hotspot extends Command {

    public hotspot(){
        //sets name, aliases, help info etc.
        super.name = "hotspot";
        super.aliases = new String[]{"hot", "h", "hs"};
        super.arguments = "[Country Code (2 Letters)] [State Code (2 Letters)] [County Name]";
        super.help = "gets recently birded hotspots in a region.";
        super.category = new Category("eBird API Integration");
    }

    @Override
    protected void execute(CommandEvent event) {

        //splits up the arguments provided
        String[] args = event.getArgs().split(" ");

        //initializes variables
        String locCode; //basically for the url
        int back = 7; //queries back a total of 7 days
        int results = 40; //array limited to 40 results as to not spam the chat

        //basically if there's no args provided it can't do squat
        if(args.length < 1){
            event.reply("This command requires at least 2 arguments, please use %help for additional help");
            return;
        } else if(args.length >= 1){
            event.reply("Getting recent hotspot visits!");
            String countryCode = args[0].toUpperCase(); //takes the country code and sticks it on into locCode
            locCode = countryCode;
            if(args.length >= 2){
                String stateCode = args[1].toUpperCase(); //adds the state code to country code if applicable
                locCode = countryCode + "-" + stateCode;

                if(args.length >= 3){ //does a boatload to get the county id number here
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
                                locCode = countyCode; //reassigns locCode to the county code
                                break;
                            }
                        }
                    } catch(IOException | RuntimeException e){
                        event.replyError("Invalid location or Drew screwed up.");
                    }
                }
            }
            //establishes a new URL to grab from, given the locCode from before
            String urlToUse = "https://api.ebird.org/v2/ref/hotspot/" + locCode + "?back=" + (back) + "&fmt=json";
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
                JSONArray sortedJsonArray = new JSONArray();

                //this whole section sorts everything by date
                List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                for (int i = 0; i < observationArray.length(); i++) {
                    jsonValues.add(observationArray.getJSONObject(i));
                }
                Collections.sort( jsonValues, new Comparator<JSONObject>() {
                    private static final String KEY_NAME = "latestObsDt";
                    public int compare(JSONObject a, JSONObject b) {
                        String valA = new String();
                        String valB = new String();
                        try {
                            valA = (String) a.get(KEY_NAME);
                            valB = (String) b.get(KEY_NAME);
                        }
                        catch (JSONException e) {}
                        return -valA.compareTo(valB);
                    }
                });

                //sticks everything into a sorted array
                for (int i = 0; i < observationArray.length(); i++) {
                    sortedJsonArray.put(jsonValues.get(i));
                }

                //basically this limits the results to 40
                String[] messageContent = new String[observationArray.length()];
                if(messageContent.length > results + 1){
                    String[] checker = new String[results + 1];
                    messageContent = checker;
                }

                //creates a title line, gets each hotspot and associated data
                messageContent[0] = "**THE " + (messageContent.length-1) + " MOST RECENTLY BIRDED HOTSPOTS FOR " + locCode + "**";
                for (int i = 1; i < messageContent.length; i++) {
                    JSONObject json = sortedJsonArray.getJSONObject(i-1 );
                    String location = json.getString("locName");
                    String latestDate = json.getString("latestObsDt");
                    String numSpecies = String.valueOf(json.getLong("numSpeciesAllTime"));
                    String individual = "\n***" + location + "*** *(all time species count " + numSpecies + ")* was last birded on " + latestDate;
                    messageContent[i] = individual;
                }
                String finalMessage = Arrays.toString(messageContent);
                finalMessage = finalMessage.substring(1, finalMessage.length() - 1);
                event.reply(finalMessage);
            } catch(IOException | RuntimeException e){
                event.reply(e.toString());
                event.replyError("No hotspots recently birded within region specified. Check for any other error messages. If you think this is inaccurate, please tell @drew#3549.");
                System.out.println(e);
            }
        }
    }
}

