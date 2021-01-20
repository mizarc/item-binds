package xyz.mizarc.persistentitems.commands.PISubCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.mizarc.persistentitems.DatabaseConnection;
import xyz.mizarc.persistentitems.Item;
import xyz.mizarc.persistentitems.ItemContainer;
import xyz.mizarc.persistentitems.PersistentItems;
import xyz.mizarc.persistentitems.commands.SubCommand;

import java.util.UUID;

public class HideCommand implements SubCommand {
    PersistentItems plugin;

    public HideCommand(PersistentItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("You must specify the name of an item");
            return false;

        } else if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to run this command without the player argument");
                return false;
            }

            if (!isItemExists(args[0])) {
                sender.sendMessage("That item does not exist");
                return false;
            }

            Player player = (Player) sender;
            addEntry(player, args[0]);
            removeFromInventory(player, args[0]);
            return true;
        }

        if (!isItemExists(args[0])) {
            sender.sendMessage("That item does not exist");
            return false;
        }


        if (Bukkit.getServer().getPlayer(args[1]) == null) {
            sender.sendMessage("That player is not online");
            return false;
        }

        Player player = (Player) sender;
        addEntry(player, args[0]);
        removeFromInventory(player, args[0]);
        return true;
    }

    private boolean isItemExists(String string) {
        ItemContainer container = plugin.getItemContainer();
        return container.getItem(string) != null;
    }

    private void addEntry(Player player, String itemName) {
        DatabaseConnection database = new DatabaseConnection(plugin);
        if (!database.isHidden(player.getUniqueId().toString(), itemName, "global")) {
            database.addHidden(player.getUniqueId().toString(), itemName, "global");
        }
    }

    private void removeFromInventory(Player player, String itemName) {
        ItemContainer container = plugin.getItemContainer();
        Item item = container.getItem(itemName);
        ItemStack itemStack = item.getItemStack(plugin);

        if (player.getInventory().contains(itemStack)) {
            player.getInventory().remove(itemStack);
        }
    }
}
