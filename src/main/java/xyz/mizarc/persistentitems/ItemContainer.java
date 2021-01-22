package xyz.mizarc.persistentitems;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

public class ItemContainer {
    private PersistentItems plugin;
    private Set<Item> activeItems = new HashSet<>();

    public ItemContainer(PersistentItems plugin) {
        this.plugin = plugin;
    }

    public void loadActiveItems() {
        activeItems.addAll(plugin.getItemConfig().getActiveItems());
    }

    public boolean loadItem(Item item) {
        return activeItems.add(item);
    }

    public Item getItem(String id) {
        for (Item item : activeItems) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public Set<Item> getAllItems() {
        return activeItems;
    }

    public static boolean isInventoryHasPItem(Plugin plugin, PlayerInventory inventory, String itemId) {
        NamespacedKey key = new NamespacedKey(plugin, "persistent");

        for (ItemStack invSlot : inventory) {
            ItemMeta itemMeta = invSlot.getItemMeta();
            if (itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING) != null) {
                return true;
            }
        }
        return false;
    }
}
