package xyz.mizarc.persistentitems.commands.persistentitems;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import xyz.mizarc.persistentitems.ItemConfigIO;

@CommandAlias("persistentitems|pitems|pi")
public class RemoveCommand extends BaseCommand {

    @Dependency
    ItemConfigIO itemConfig;

    @Subcommand("remove")
    @CommandPermission("persistentitems.command.remove")
    @CommandCompletion("@pitems")
    @Syntax("<item>")
    public void onRemove(CommandSender sender, String itemId) {
        if (!itemConfig.removeItem(itemId)) {
            sender.sendMessage("An item of that id doesn't exist");
            return;
        }
        sender.sendMessage("Item " + itemId + " has been removed");
    }
}
