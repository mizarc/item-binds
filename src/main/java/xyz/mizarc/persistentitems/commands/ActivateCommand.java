package xyz.mizarc.persistentitems.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.mizarc.persistentitems.Item;
import xyz.mizarc.persistentitems.ItemConfigIO;
import xyz.mizarc.persistentitems.PersistentItems;

public class ActivateCommand implements CommandExecutor {
    PersistentItems plugin;

    ActivateCommand(PersistentItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("No arguments specified");
            return false;
        }

        Item item = plugin.getItemConfig().getItem(args[0]);
        if (item == null) {
            return true;
        }

        ItemConfigIO itemConfig = plugin.getItemConfig();
        plugin.getItemContainer().loadItem(itemConfig.getItem(args[0]));
        itemConfig.setActive(args[0], true);
        return true;
    }
}
