package parkourHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parkourHelper.util.Path;

import java.util.Collections;
import java.util.List;

public class CommandHandler extends CommandBase {

    private static Logger LOGGER = LogManager.getLogger(ParkourHelper.MODID);

    @Override
    public String getCommandName() {
        return ParkourHelper.MODID;
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("pkh");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/pkh set <variable> <value>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("set")) {
                Integer number = null;
                if (args.length >= 3) {
                    try {
                        number = Integer.parseInt(args[2]);
                    } catch (NumberFormatException ex) {
                        throw new CommandException("notUse");
                    }
                }
                if (number == null) {
                    throw new CommandException("failed");
                }

                if (args[1].equalsIgnoreCase("xPos")) {
                    ConfigHandler.xPos = number;
                    ConfigHandler.saveConfig();
                    LOGGER.log(Level.INFO, "saving xPos");
                } else if (args[1].equalsIgnoreCase("yPos")) {
                    ConfigHandler.yPos = number;
                    ConfigHandler.saveConfig();
                    LOGGER.log(Level.INFO, "saving yPos");
                } else {
                    LOGGER.log(Level.ERROR, "Key was wrong, not xPos or yPos");
                    throw new CommandException("invalKey");
                }
            } else if (args[0].equalsIgnoreCase("toggleDraw")) {
                ParkourHelper.pathDrawer.doPathDrawing = !ParkourHelper.pathDrawer.doPathDrawing;
            } else if (args[0].equalsIgnoreCase("newPath")) {
                ParkourHelper.pathDrawer.startNewPath();
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("not valid, did you mean to add a set before value?"));
            }
        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(getCommandUsage(null)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}