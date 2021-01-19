package xyz.mizarc.persistentitems;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String id;
    private ItemStack item;
    private int slot;
    private List<String> commands = new ArrayList<>();

    public Item(String id, ItemStack item, Integer slot) {
        this.id = id;
        this.item = item;
        this.slot = slot;
    }

    public Item(String id, ItemStack item, Integer slot, List<String> commands) {
        this.id = id;
        this.item = item;
        this.slot = slot;
        this.commands = commands;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = id;
    }

    public ItemStack getItemStack(Plugin plugin) {
        ItemStack itemStack = item;
        ItemMeta itemMeta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "persistent");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, getId());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void addCommand(String command) {
        commands.add(command);
    }

    public void removeCommand(int index) {
        commands.remove(index);
    }
}
