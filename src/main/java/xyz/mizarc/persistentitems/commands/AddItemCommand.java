package xyz.mizarc.persistentitems.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import xyz.mizarc.persistentitems.PersistentItems;

public class AddItemCommand implements CommandExecutor {
    private PersistentItems plugin;

    public AddItemCommand(PersistentItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage("No arguments specified");
            return false;
        }

        Player player = (Player) sender;
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        plugin.getItemConfig().addItem(args[0]);

        return true;
    }
}
