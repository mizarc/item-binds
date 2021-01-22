package xyz.mizarc.persistentitems.commands.persistentitems;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import xyz.mizarc.persistentitems.DatabaseConnection;
import xyz.mizarc.persistentitems.Item;
import xyz.mizarc.persistentitems.ItemContainer;
import xyz.mizarc.persistentitems.PersistentItems;

import java.util.Arrays;
import java.util.List;

@CommandAlias("persistentitems|pitems|pi")
public class ShowCommand extends BaseCommand {

    @Dependency
    PersistentItems plugin;

    @Dependency
    ItemContainer itemContainer;

    @Subcommand("show")
    @CommandPermission("persistentitems.command.show")
    @CommandCompletion("@pitems @players")
    @Syntax("<item> [player]")
    public void onShow(CommandSender sender, String itemId, @Optional String specifiedPlayerName) {
        // Error if console is trying to use this without specifying a player
        if (!(sender instanceof Player) && specifiedPlayerName == null) {
            sender.sendMessage("You must specify the player argument as the console");
            return;
        }

        // Error if persistent item is not active
        if (itemContainer.getItem(itemId) == null) {
            sender.sendMessage("Item " + itemId + " does not exist");
            return;
        }

        // Add item to specified player's inventory unless player already has it
        if (specifiedPlayerName != null) {
            Player specifiedPlayer = Bukkit.getPlayer(specifiedPlayerName);
            if (specifiedPlayer == null) {
                sender.sendMessage("That player is not online");
            }

            if (!addToInventory(specifiedPlayer.getInventory(), itemId)) {
                sender.sendMessage("Item " + itemId + " is already in " + specifiedPlayer.getDisplayName() + "'s inventory");
                return;
            }
            removeFromDatabase(specifiedPlayer.getUniqueId().toString(), itemId);
            sender.sendMessage("Item " + itemId + " has been added to " + specifiedPlayer.getDisplayName() + "'s inventory");
            return;
        }

        //Add item to own inventory unless you already have it
        Player player = (Player) sender;
        if (!addToInventory(player.getInventory(), itemId)) {
            sender.sendMessage("Item " + itemId + " is already in your inventory");
            return;
        }
        removeFromDatabase(player.getUniqueId().toString(), itemId);
        sender.sendMessage("Item " + itemId + " has been added to your inventory");
    }

    private boolean addToInventory(PlayerInventory inventory, String itemId) {
        List<ItemStack> presentItems = ItemContainer.getPItemsInInventory(plugin, inventory, itemId);
        if (!presentItems.isEmpty()) {
            return false;
        }

        // True if the inventory doesn't have the item and attempt to add the item to the specified slot.
        // Add to any slot if that slot is occupied
        Item item = itemContainer.getItem(itemId);
        ItemStack itemStack = item.getItemStack(plugin);
        if (inventory.getItem(item.getSlot()) == null) {
            inventory.setItem(item.getSlot(), itemStack);
            return true;
        }
        inventory.addItem(itemStack);
        return true;
    }

    private void removeFromDatabase(String playerId, String itemName) {
        DatabaseConnection database = new DatabaseConnection(plugin);
        database.removeHidden(playerId, itemName, "global");
        database.closeConnection();
    }
}
