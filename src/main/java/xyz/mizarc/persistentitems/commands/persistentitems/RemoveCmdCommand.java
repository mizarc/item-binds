package xyz.mizarc.persistentitems.commands.persistentitems;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import xyz.mizarc.persistentitems.Item;
import xyz.mizarc.persistentitems.ItemConfigIO;
import xyz.mizarc.persistentitems.ItemContainer;

@CommandAlias("persistentitems|pitems|pi")
public class RemoveCmdCommand extends BaseCommand {

    @Dependency
    ItemConfigIO itemConfig;

    @Dependency
    ItemContainer itemContainer;

    @Subcommand("removecmd")
    @CommandPermission("persistentitems.command.removecmd")
    @CommandCompletion("@pitems @nothing")
    @Syntax("<item> <command>")
    public void onRemoveCmd(CommandSender sender, String itemId, int index) {
        Item item = itemConfig.getItem(itemId);
        if (item == null) {
            sender.sendMessage("Item " + itemId + " does not exist.");
            return;
        }

        if (itemContainer.getItem(itemId).getCommand(index - 1) == null) {
            sender.sendMessage("There's no command at priority " + index + ".");
            return;
        }

        // Add assigned command to item
        itemContainer.getItem(itemId).removeCommand(index);
        itemConfig.removeCommand(itemId, index);
        sender.sendMessage("Removed command at priority " + itemId + ".");
    }
}
