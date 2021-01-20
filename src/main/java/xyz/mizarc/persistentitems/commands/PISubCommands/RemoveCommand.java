package xyz.mizarc.persistentitems.commands.PISubCommands;

import org.bukkit.command.CommandSender;
import xyz.mizarc.persistentitems.PersistentItems;
import xyz.mizarc.persistentitems.commands.SubCommand;

public class RemoveCommand implements SubCommand {
    private PersistentItems plugin;

    public RemoveCommand(PersistentItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
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
