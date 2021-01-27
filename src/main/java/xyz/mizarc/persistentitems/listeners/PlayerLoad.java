package xyz.mizarc.persistentitems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.mizarc.persistentitems.DatabaseConnection;
import xyz.mizarc.persistentitems.Item;
import xyz.mizarc.persistentitems.PersistentItems;

import java.util.Set;

public class PlayerLoad implements Listener {
    private PersistentItems plugin;

    public PlayerLoad(PersistentItems plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        giveItemsIfMissing(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        giveItemsIfMissing(event.getPlayer());
    }

    private void giveItemsIfMissing(Player player) {
        Inventory inventory = player.getInventory();
        Set<Item> activeItems = plugin.getItemContainer().getAllItems();

        // Loop through all active items
        for (Item activeItem : activeItems) {
            DatabaseConnection database = new DatabaseConnection(plugin);

            // Cancel if item is on player's ignore list
            if (database.isHidden(player.getUniqueId().toString(), activeItem.getId(), "global")) {
                database.closeConnection();
                break;
            }

            // Scan inventory for persistent item
            String itemId = activeItem.getId();
            if (!checkInventoryForItem(inventory, itemId)) {
                break;
            }

            giveItem(player, activeItem);
        }
    }

    private void giveItem(Player player, Item item) {
        Inventory inventory = player.getInventory();

        if (inventory.getItem(item.getSlot()) == null) {
            inventory.setItem(item.getSlot(), item.getItemStack(plugin));
            return;
        }
        inventory.addItem(item.getItemStack(plugin));
    }

    private boolean checkInventoryForItem(Inventory inventory, String itemId) {
        for (ItemStack itemStack : inventory) {
            if (itemStack == null) {
                continue;
            }

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null) {
                continue;
            }

            // Go to next item if this item doesn't have metadata
            NamespacedKey key = new NamespacedKey(plugin, "persistent");
            if (itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING) == null) {
                continue;
            }

            // Go to next item if player already has item
            if (itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING).equals(itemId)) {
                return false;
            }
        }
        return true;
    }
}
