package xyz.mizarc.persistentitems.commands.persistentitems;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import xyz.mizarc.persistentitems.DatabaseConnection;
import xyz.mizarc.persistentitems.Item;
import xyz.mizarc.persistentitems.ItemContainer;
import xyz.mizarc.persistentitems.PersistentItems;

import java.util.Arrays;

@CommandAlias("persistentitems|pitems|pi")
public class HideCommand extends BaseCommand {

    @Dependency
    private PersistentItems plugin;

    @Dependency
    ItemContainer itemContainer;

    @Subcommand("hide")
    @CommandPermission("persistentitems.command.hide")
    @CommandCompletion("@pitems @player")
    @Syntax("<item> [player]")
    public void onHide(CommandSender sender, String itemId, @Optional Player specifiedPlayer) {
        // Error if console is trying to use this without specifying a player
        if (!(sender instanceof Player) && specifiedPlayer == null) {
            sender.sendMessage("You must specify the player argument as the console");
            return;
        }

        // Error if persistent item is not active
        if (itemContainer.getItem(itemId) == null) {
            sender.sendMessage("Item " + sender + " does not exist");
            return;
        }

        // Add item to specified player's inventory unless player already has it
        if (specifiedPlayer != null) {
            if (!removeFromInventory(specifiedPlayer.getInventory(), itemId)) {
                sender.sendMessage("Item " + itemId + " is not in " + specifiedPlayer + "'s inventory");
                return;
            }
            addToDatabase(specifiedPlayer.getUniqueId().toString(), itemId);
            sender.sendMessage("Item " + itemId + " has been removed from " + specifiedPlayer + "'s inventory");
            return;
        }

        // Remove item from own inventory unless you don't have it
        Player player = (Player) sender;
        if (!removeFromInventory(player.getInventory(), itemId)) {
            sender.sendMessage("Item " + itemId + " is not in your inventory");
            return;
        }
        addToDatabase(player.getUniqueId().toString(), itemId);
        sender.sendMessage("Item " + itemId + " has been removed from your inventory");
    }

    private boolean removeFromInventory(PlayerInventory inventory, String itemName) {
        ItemContainer container = plugin.getItemContainer();
        Item item = container.getItem(itemName);
        ItemStack itemStack = item.getItemStack(plugin);

        // False if the inventory doesn't have the item
        if (inventory.contains(itemStack)) {
            return false;
        } else if (inventory.getItemInOffHand().equals(itemStack)) {
            return false;
        } else if (Arrays.asList(inventory.getArmorContents()).contains(itemStack)) {
            return false;
        }

        // True if the inventory has the item and remove it
        inventory.remove(itemStack);
        return true;
    }

    private void addToDatabase(String playerId, String itemName) {
        DatabaseConnection database = new DatabaseConnection(plugin);
        database.addHidden(playerId, itemName, "global");
        database.closeConnection();
    }
}
