package xyz.mizarc.persistentitems;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;

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

    public static List<ItemStack> getPItemsInInventory(Plugin plugin, PlayerInventory inventory, String itemId) {
        NamespacedKey key = new NamespacedKey(plugin, "persistent");

        // Add each individual ItemStack with persistent metadata in inventory to list
        List<ItemStack> presentItems = new ArrayList<>();
        for (ItemStack itemSlot : inventory.getContents()) {
            if (itemSlot == null) {
                continue;
            }
            ItemMeta itemMeta = itemSlot.getItemMeta();
            if (itemMeta == null) {
                continue;
            }
            if (itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING).equals(itemId)) {
                presentItems.add(itemSlot);
            }
        }
        return presentItems;
    }

    public static List<Integer> getSlotsWithPItems(Plugin plugin, PlayerInventory inventory, String itemId) {
        NamespacedKey key = new NamespacedKey(plugin, "persistent");

        // Add each individual ItemStack with persistent metadata in inventory to list
        List<Integer> presentItemSlots = new ArrayList<>();
        for (int i=0; i < inventory.getContents().length; i++)
        {
            if (inventory.getContents()[i] == null) {
                continue;
            }
            ItemMeta itemMeta = inventory.getContents()[i].getItemMeta();
            if (itemMeta == null) {
                continue;
            }
            if (itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING).equals(itemId)) {
                presentItemSlots.add(i);
            }
        }
        return presentItemSlots;
    }
}
