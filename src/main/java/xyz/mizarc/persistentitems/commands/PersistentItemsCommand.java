package xyz.mizarc.persistentitems.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import xyz.mizarc.persistentitems.PersistentItems;
import xyz.mizarc.persistentitems.commands.PISubCommands.ActivateCommand;
import xyz.mizarc.persistentitems.commands.PISubCommands.AddCommand;
import xyz.mizarc.persistentitems.commands.PISubCommands.AddCommandCommand;
import xyz.mizarc.persistentitems.commands.PISubCommands.RemoveCommand;

import java.util.*;

public class PersistentItemsCommand implements CommandExecutor, TabCompleter {
    private PersistentItems plugin;
    private Map<String, SubCommand> subcommands = new HashMap<>();

    public PersistentItemsCommand(PersistentItems plugin) {
        this.plugin = plugin;
        subcommands.put("add", new AddCommand(plugin));
        subcommands.put("remove", new RemoveCommand(plugin));
        subcommands.put("activate", new ActivateCommand(plugin));
        subcommands.put("addcommand", new AddCommandCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = subcommands.get(args[0]);

            if (subCommand == null) {
                sender.sendMessage("That is not a valid subcommand");
                return false;
            }
            subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> subCommandNames = new ArrayList<>();
        subCommandNames.addAll(subcommands.keySet());
        return subCommandNames;
    }
}