package xyz.mizarc.persistentitems.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
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

        for (Item activeItem : activeItems) {
            if (inventory.contains(activeItem.getItemStack(plugin))) {
                break;
            }

            DatabaseConnection connection = new DatabaseConnection(plugin);
            if (connection.isHidden(player.getUniqueId().toString(), activeItem.getId(), "global")) {
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
}
