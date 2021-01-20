package xyz.mizarc.persistentitems.commands.PISubCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.mizarc.persistentitems.DatabaseConnection;
import xyz.mizarc.persistentitems.Item;
import xyz.mizarc.persistentitems.ItemContainer;
import xyz.mizarc.persistentitems.PersistentItems;
import xyz.mizarc.persistentitems.commands.SubCommand;

import java.util.UUID;

public class ShowCommand implements SubCommand {
    PersistentItems plugin;

    public ShowCommand(PersistentItems plugin) {
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
            removeEntry(player, args[0]);
            addToInventory(player, args[0]);
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
        removeEntry(player, args[0]);
        addToInventory(player, args[0]);
        return true;
    }

    private boolean isItemExists(String string) {
        ItemContainer container = plugin.getItemContainer();
        return container.getItem(string) != null;
    }

    private void removeEntry(Player player, String itemName) {
        DatabaseConnection database = new DatabaseConnection(plugin);
        if (!database.isHidden(player.getUniqueId().toString(), itemName, "global")) {
            database.removeHidden(player.getUniqueId().toString(), itemName, "global");
        }
    }

    private void addToInventory(Player player, String itemName) {
        ItemContainer container = plugin.getItemContainer();
        Item item = container.getItem(itemName);
        ItemStack itemStack = item.getItemStack(plugin);

        if (!player.getInventory().contains(itemStack)) {
            Inventory inventory = player.getInventory();

            if (inventory.getItem(item.getSlot()) == null) {
                inventory.setItem(item.getSlot(), itemStack);
                return;
            }
            inventory.addItem(itemStack);
        }
    }
}
