package xyz.mizarc.persistentitems.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Description;
import org.bukkit.command.CommandSender;
import xyz.mizarc.persistentitems.PersistentItems;

@CommandAlias("pi")
@Description("The base command for all Persistent Item actions")
public class PersistentItemsCommand extends BaseCommand {

    @Dependency
    private PersistentItems plugin;

    @Default
    public void onPersistentItems(CommandSender sender) {
        sender.sendMessage("Hi");
    }
}
