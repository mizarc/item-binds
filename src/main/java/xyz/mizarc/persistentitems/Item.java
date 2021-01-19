package xyz.mizarc.persistentitems;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private ItemStack item;
    private int slot;
    private List<String> commands = new ArrayList<>();

    public ItemStack getItem() {
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
