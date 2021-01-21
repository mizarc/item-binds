package xyz.mizarc.persistentitems.commands.PISubCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import xyz.mizarc.persistentitems.Item;
import xyz.mizarc.persistentitems.ItemConfigIO;
import xyz.mizarc.persistentitems.ItemContainer;

@CommandAlias("persistentitems|pitems|pi")
public class AddCommandCommand extends BaseCommand {

    @Dependency
    ItemConfigIO itemConfig;

    @Dependency
    ItemContainer itemContainer;

    @Subcommand("addcommand")
    @CommandPermission("persistentitems.command.addcommand")
    @CommandCompletion("@pitems @nothing")
    @Syntax("<item> <command>")
    public void onAddCommand(CommandSender sender, String itemId, String[] commandArray) {
        Item item = itemConfig.getItem(itemId);
        if (item == null) {
            sender.sendMessage("Item " + itemId + " does not exist.");
        }

        // Concatenate command to be assigned
        StringBuilder stringBuilder = new StringBuilder();
        for (String commandWord : commandArray) {
            stringBuilder.append(commandWord);
        }
        String itemCommand = stringBuilder.toString();

        // Add assigned command to item
        itemContainer.getItem(itemId).addCommand(itemCommand);
        itemConfig.addCommand(itemId, itemCommand);
        sender.sendMessage("Command has been assigned to item " + itemId);
    }
}
