package xyz.mizarc.persistentitems.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.mizarc.persistentitems.ItemContainer;
import xyz.mizarc.persistentitems.PersistentItems;

public class AddFunctionalityCommand implements CommandExecutor {
    PersistentItems plugin;

    public AddFunctionalityCommand(PersistentItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("No arguments specified");
            return false;
        } else if (args.length == 1) {
            sender.sendMessage("Command argument not specified");
            return false;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i=2; i < args.length; i++) {
            stringBuilder.append(args[i]);
        }
        String itemCommand = stringBuilder.toString();

        plugin.getItemConfig().addCommand(args[0], itemCommand);
        sender.sendMessage("Added command to '" + args[0] + "'");

        ItemContainer container = plugin.getItemContainer();
        if (container.getItem(args[0]) == null) {
            return true;
        }

        container.getItem(args[0]).addCommand(itemCommand);
        return true;
    }
}
