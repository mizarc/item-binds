package xyz.mizarc.persistentitems.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
        Inventory inventory = event.getPlayer().getInventory();
        ItemStack[] contents = inventory.getContents();

        Set<Item> activeItems = plugin.getItemContainer().getAllItems();

        for (Item activeItem : activeItems) {
            for (ItemStack item : contents) {
                if (activeItem.getItemStack(plugin) == item) {
                    return;
                }
            }
            giveItem(event.getPlayer(), activeItem);
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