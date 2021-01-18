package xyz.mizarc.persistentitems.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.mizarc.persistentitems.PersistentItems;

public class RemoveItemCommand implements CommandExecutor {
    private PersistentItems plugin;

    public RemoveItemCommand(PersistentItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("No arguments specified");
            return false;
        }

        boolean removeResult = plugin.getItemConfig().removeItem(args[0]);
        if (!removeResult) {
            sender.sendMessage("That persistent item doesn't exist");
            return true;
        }
        sender.sendMessage("Persistent item of id " + args[0] + " has been removed");
        return true;
    }
}
