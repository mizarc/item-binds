package xyz.mizarc.persistentitems.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import xyz.mizarc.persistentitems.PersistentItems;

@CommandAlias("persistentitems|pitems|pi")
@CommandPermission("persistentitems.command")
@Description("The base command for all Persistent Item actions")
public class PersistentItemsCommand extends BaseCommand {

    @Dependency
    private PersistentItems plugin;

    @Default
    public void onPersistentItems(CommandSender sender) {
        sender.sendMessage("Hi");
    }
}
