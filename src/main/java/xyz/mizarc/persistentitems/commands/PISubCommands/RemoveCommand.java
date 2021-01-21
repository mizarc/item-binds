package xyz.mizarc.persistentitems.commands.PISubCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import xyz.mizarc.persistentitems.ItemConfigIO;

@CommandAlias("pi")
public class RemoveCommand extends BaseCommand {

    @Dependency
    ItemConfigIO itemConfig;

    @Subcommand("remove")
    public void onRemove(CommandSender sender, String itemId) {
        if (!itemConfig.removeItem(itemId)) {
            sender.sendMessage("An item of that id doesn't exist");
            return;
        }
        sender.sendMessage("Item " + itemId + " has been removed");
    }
}
