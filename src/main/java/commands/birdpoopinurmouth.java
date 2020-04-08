package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class birdpoopinurmouth extends Command {

    public birdpoopinurmouth(){
        //sets name, aliases, help info etc.
        super.name = "poop";
        super.aliases = new String[]{"p"};
        super.arguments = "[number][unit of time]";
        super.help = "converts time into a practically useless unit (source: wolfram alpha).";
        super.category = new Category("Random");
    }

    @Override
    protected void execute(CommandEvent event){
        //time (in years) that it would take, according to Wolfram
        final double constant = 193;

        //splits and parses arguments, assigns values to variables
        String[] args = event.getArgs().split(" ");
        if(args.length == 1 || args.length == 0){
            //if no second parameter specified, returns this in a message
            event.reply("This command requires two arguments, usage: `%poop [number] [unit of time]`.");
            return;
        }
        double number = Double.parseDouble(args[0]);
        String convertTo = args[1];

        //initializing final number variable
        double fin = 0;

        //years --> poop
        if(convertTo.equalsIgnoreCase("years") || convertTo.equalsIgnoreCase("year")){
            fin = number/constant;
        }

        //days --> poop
        if(convertTo.equalsIgnoreCase("days") || convertTo.equalsIgnoreCase("days")){
            fin = number/365/constant;
        }

        //hours --> poop
        if(convertTo.equalsIgnoreCase("hours") || convertTo.equalsIgnoreCase("hour")){
            fin = number/24/365/constant;
        }

        //minutes --> poop
        if(convertTo.equalsIgnoreCase("minute") || convertTo.equalsIgnoreCase("minutes")){
            fin = number/60/24/365/constant;
        }

        //seconds --> poop
        if(convertTo.equalsIgnoreCase("second") || convertTo.equalsIgnoreCase("seconds")){
            fin = number/60/60/24/365/constant;
        }

        //replies with this, sends the reply
        String reply = "A bird would poop in your mouth--assuming you are lying down with your mouth open--approximately " + fin + " times over in the amount of time specified.";
        event.reply(reply);
    }

}
