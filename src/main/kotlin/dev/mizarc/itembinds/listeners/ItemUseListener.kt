package dev.mizarc.itembinds.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import dev.mizarc.itembinds.ItemRepository
import java.util.*

class ItemUseListener(private val itemRepo: ItemRepository) : Listener {
    @EventHandler
    fun onRightClick(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.item == null)  return

        val itemStack = event.item?: return
        val itemMeta = itemStack.itemMeta
        val key = NamespacedKey("persistentitems", "item")
        if (!itemMeta.persistentDataContainer.has(key, PersistentDataType.STRING)) {
            return
        }
        val id = itemMeta.persistentDataContainer.get(key, PersistentDataType.STRING)
        val item = itemRepo.getById(UUID.fromString(id)) ?: return
        for (command in item.commands) {
            event.player.performCommand(command)
        }
    }
}