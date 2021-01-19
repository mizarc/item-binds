package xyz.mizarc.persistentitems.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.mizarc.persistentitems.Item;
import xyz.mizarc.persistentitems.PersistentItems;

import java.util.HashSet;
import java.util.Set;

public class PreventItemRemoval implements Listener {
    private PersistentItems plugin;

    public PreventItemRemoval(PersistentItems plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();

        Set<Item> activeItems = plugin.getItemContainer().getAllItems();
        Set<ItemStack> activeItemStacks = new HashSet<>();
        for (Item activeItem : activeItems) {
            activeItemStacks.add(activeItem.getItemStack(plugin));
        }

        if (activeItemStacks.contains(itemStack)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMove(InventoryClickEvent event) {
        ItemStack itemStack = event.getCursor();
        Inventory otherInv = event.getView().getTopInventory();

        Set<Item> activeItems = plugin.getItemContainer().getAllItems();
        Set<ItemStack> activeItemStacks = new HashSet<>();
        for (Item activeItem : activeItems) {
            activeItemStacks.add(activeItem.getItemStack(plugin));
        }

        if (activeItemStacks.contains(itemStack) && event.getClickedInventory() == otherInv) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMove(InventoryDragEvent event) {
        ItemStack itemStack = event.getOldCursor();
        Inventory otherInv = event.getView().getTopInventory();

        Set<Item> activeItems = plugin.getItemContainer().getAllItems();
        Set<ItemStack> activeItemStacks = new HashSet<>();
        for (Item activeItem : activeItems) {
            activeItemStacks.add(activeItem.getItemStack(plugin));
        }

        if (activeItemStacks.contains(itemStack) && otherInv == event.getInventory()) {
            event.setCancelled(true);
        }
    }
}
