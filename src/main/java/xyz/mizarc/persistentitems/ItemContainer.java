package xyz.mizarc.persistentitems;

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

    public void loadItem(Item item) {
        activeItems.add(item);
    }

    public Item getItem(String id) {
        for (Item item : activeItems) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }
}
