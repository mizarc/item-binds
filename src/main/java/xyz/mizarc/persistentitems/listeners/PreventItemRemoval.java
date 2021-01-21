package xyz.mizarc.persistentitems.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.mizarc.persistentitems.Item;
import xyz.mizarc.persistentitems.PersistentItems;

import java.util.HashSet;
import java.util.List;
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
    public void onMoveToInventory(InventoryClickEvent event) {
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
    public void onDragToInventory(InventoryDragEvent event) {
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

    @EventHandler
    public void onItemFrameUse(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame)) {
            return;
        }

        Set<Item> activeItems = plugin.getItemContainer().getAllItems();
        Set<ItemStack> activeItemStacks = new HashSet<>();
        for (Item activeItem : activeItems) {
            activeItemStacks.add(activeItem.getItemStack(plugin));
        }

        if (activeItemStacks.contains(event.getPlayer().getInventory().getItemInMainHand())) {
            event.setCancelled(true);
        }

        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR &&
                activeItemStacks.contains(event.getPlayer().getInventory().getItemInOffHand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack itemStack = event.getItemInHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "persistent");
        if (itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING) == null) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        List<ItemStack> droppedItems = event.getDrops();

        Set<Item> activeItems = plugin.getItemContainer().getAllItems();
        Set<ItemStack> activeItemStacks = new HashSet<>();
        for (Item activeItem : activeItems) {
            activeItemStacks.add(activeItem.getItemStack(plugin));
        }

        for (ItemStack activeItemStack : activeItemStacks) {
            if (droppedItems.contains(activeItemStack)) {
                droppedItems.remove(activeItemStack);
            }
        }
    }
}
