package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class calculator extends Command {

    public calculator(){
        //sets name, aliases, help info etc.
        super.name = "calc";
        super.aliases = new String[]{"calculator", "calculate"};
        super.help = "provides answers to basic arithmetic questions.";
        super.arguments = "[add/sub/mult/div] [val1] [val2]...";
        super.category = new Category("Misc.");
    }

    @Override
    protected void execute(CommandEvent event) {
        //splits up message into individual components
        String[] messageSent = event.getMessage().getContentRaw().split(" ");

        //returns error if not enough parameters
        if (messageSent.length <= 3) {
            event.reply("Calculator requires 3 parameters. **Calculator usage:** '%calculate [add/sub/mult/div] [val1] [val2]...'");
            return;

        //code for addition
        } else if (messageSent[1].equalsIgnoreCase("add") || messageSent[1].equalsIgnoreCase("+")) {
            double result = Double.parseDouble(messageSent[2]);
            for (int i = 3; i < messageSent.length; i++) {
                result += Double.parseDouble(messageSent[i]);
            }
            event.getChannel().sendMessage("Result: " + result).queue();

        //code for subtraction
        } else if (messageSent[1].equalsIgnoreCase("sub") || messageSent[1].equalsIgnoreCase("-") || messageSent[1].equalsIgnoreCase("subtract")) {
            double result = Double.parseDouble(messageSent[2]);
            for (int i = 3; i < messageSent.length; i++) {
                result -= Double.parseDouble(messageSent[i]);
            }
            event.getChannel().sendMessage("Result: " + result).queue();

        //code for multiplication
        } else if (messageSent[1].equalsIgnoreCase("mult") || messageSent[1].equalsIgnoreCase("*") || messageSent[1].equalsIgnoreCase("multiply")) {
            double result = Double.parseDouble(messageSent[2]);
            for (int i = 3; i < messageSent.length; i++) {
                result *= Double.parseDouble(messageSent[i]);
            }
            event.getChannel().sendMessage("Result: " + result).queue();

        //code for division
        } else if (messageSent[1].equalsIgnoreCase("div") || messageSent[1].equalsIgnoreCase("/") || messageSent[1].equalsIgnoreCase("divide")) {
            double result = Double.parseDouble(messageSent[2]);
            for (int i = 3; i < messageSent.length; i++) {
                result /= Double.parseDouble(messageSent[i]);
            }
            event.getChannel().sendMessage("Result: " + result).queue();

        }
    }
}
