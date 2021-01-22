package xyz.mizarc.persistentitems.listeners;

import org.bukkit.Bukkit;
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
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "persistent");
        if (itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMoveToInventory(InventoryClickEvent event) {
        if (event.getCursor() == null) {
            return;
        }

        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

        ItemStack itemStack = event.getCursor();
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "persistent");
        if (itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDragToInventory(InventoryDragEvent event) {
        ItemStack itemStack = event.getOldCursor();
        ItemMeta itemMeta = itemStack.getItemMeta();
        Inventory otherInv = event.getView().getTopInventory();

        NamespacedKey key = new NamespacedKey(plugin, "persistent");
        if (itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING) != null &&
                otherInv == event.getInventory()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemFrameUse(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame)) {
            return;
        }

        NamespacedKey key = new NamespacedKey(plugin, "persistent");
        ItemStack mainHandItem = event.getPlayer().getInventory().getItemInMainHand();
        ItemStack offHandItem = event.getPlayer().getInventory().getItemInOffHand();

        // Cancel event if main hand has item
        ItemMeta mainHandItemMeta = mainHandItem.getItemMeta();
        if (mainHandItemMeta != null) {
            if (mainHandItemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING) != null) {
                event.setCancelled(true);
            }
        }

        // Cancel event if offhand has item and main hand is empty
        ItemMeta offHandItemMeta = offHandItem.getItemMeta();
        if (offHandItemMeta != null) {
            if (offHandItemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING) != null &&
                    mainHandItem.getType() == Material.AIR) {
                event.setCancelled(true);
            }
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

        NamespacedKey key = new NamespacedKey(plugin, "persistent");
        for (ItemStack droppedItem : droppedItems) {
            ItemMeta itemMeta = droppedItem.getItemMeta();
            if (itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING) != null) {
                droppedItems.remove(droppedItem);
            }
        }
    }
}
