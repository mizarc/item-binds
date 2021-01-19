package xyz.mizarc.persistentitems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.mizarc.persistentitems.Item;
import xyz.mizarc.persistentitems.PersistentItems;

public class RunLinkedCommand implements Listener {
    private PersistentItems plugin;

    public RunLinkedCommand(PersistentItems plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }

        ItemStack itemStack = event.getItem();
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "persistent");

        if (!itemMeta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            return;
        }

        String id = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        Item item = plugin.getItemContainer().getItem(id);

        if (item.getCommands() == null) {
            return;
        }

        for (String command : item.getCommands()) {
            event.getPlayer().performCommand(command);
        }
    }
}
