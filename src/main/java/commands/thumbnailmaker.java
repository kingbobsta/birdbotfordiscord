package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URL;

public class thumbnailmaker extends Command {

    public thumbnailmaker(){
        super.name = "thumbnail";
        super.arguments = "[image-url] [new width] [new height] [degrees to rotate (optional)]";
        super.aliases = new String[]{"shrink", "resize", "imageresize", "shrinkimage"};
        super.help = "resizes and/or rotates images and generates thumbnails";
        super.category = new Category("Misc.");
    }

    @Override
    protected void execute(CommandEvent event){
        if(event.getArgs().equalsIgnoreCase("")){
            event.reply("No arguments provided " + event.getAuthor().getAsMention() + " Please use %thumbnail [image-url] [new width] [new height] [degrees to rotate (optional)");
        }
        else {
            try {
                String[] args = event.getArgs().split(" ");
                int width = Integer.parseInt(args[1]);
                int height = Integer.parseInt(args[2]);
                URL imageURL = new URL(args[0]);
                int rotateAmount = 0;
                if(args.length == 4){
                    rotateAmount = Integer.parseInt(args[3]);
                }
                OutputStream os = new ByteArrayOutputStream();
                Thumbnails.of(imageURL).forceSize(width,height).rotate(rotateAmount).outputFormat("png").toOutputStream(os);
                byte[] imageInBytes = ((ByteArrayOutputStream) os).toByteArray();
                event.getChannel().sendFile(imageInBytes,"GeneratedFile.png").queue();
                event.reply(event.getAuthor().getAsMention() + ", here is your new image!");
            } catch (Exception ex) {
                System.out.println("An exception happened.");
                event.reply("An error occurred. Did you use the correct formatting? %thumbnail [image-url] [new width] [new height] [degrees to rotate (optional)");
            }
        }
    }
}
