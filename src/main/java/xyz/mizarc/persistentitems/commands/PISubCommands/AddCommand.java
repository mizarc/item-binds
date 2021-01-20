package xyz.mizarc.persistentitems.commands.PISubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.mizarc.persistentitems.PersistentItems;
import xyz.mizarc.persistentitems.commands.SubCommand;

public class AddCommand implements SubCommand {
    private PersistentItems plugin;

    public AddCommand(PersistentItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage("No arguments specified");
            return false;
        }
        else if (args.length == 1) {
            sender.sendMessage("Slot argument not specified");
            return false;
        }

        Player player = (Player) sender;
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        plugin.getItemConfig().addItem(args[0], heldItem, Integer.parseInt(args[1]));
        sender.sendMessage("Held item " + heldItem.getItemMeta().getDisplayName() + " has been added");
        return true;
    }
}