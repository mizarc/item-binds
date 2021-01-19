package xyz.mizarc.persistentitems;

import org.bukkit.inventory.ItemStack;

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

    public ItemStack getItemStack() {
        return item;
    }

    public int getSlot() {
        return slot;
    }

    public List<String> commands() {
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
