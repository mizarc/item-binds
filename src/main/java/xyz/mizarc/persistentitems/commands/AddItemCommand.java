package xyz.mizarc.persistentitems.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command");
            return false;
        }

        if (args[0] == null) {
            sender.sendMessage("Invalid arguments");
        }

        Player player = (Player) sender;
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        return false;
    }
}
