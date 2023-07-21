package dev.mizarc.persistentitems.listeners

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.ItemFrame
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.persistence.PersistentDataType
import dev.mizarc.persistentitems.PersistentItems

class ItemRemovalListener(private val plugin: PersistentItems) : Listener {
    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        val itemStack = event.itemDrop.itemStack
        val itemMeta = itemStack.itemMeta
        val key = NamespacedKey("persistentitems", "item")
        if (itemMeta.persistentDataContainer.get(key, PersistentDataType.STRING) != null) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onMoveToInventory(event: InventoryClickEvent) {
        if (event.cursor == null) {
            return
        }

        // Cancel if item is in bottom
        val key = NamespacedKey("persistentitems", "item")
        if (event.clickedInventory === event.view.bottomInventory) {
            return
        }

        // Cancel if item meta doesn't exist
        val itemStack = event.cursor
        val itemMeta = itemStack!!.itemMeta ?: return

        // Check if item is trying to be placed in top slot
        if (itemMeta.persistentDataContainer.get(key, PersistentDataType.STRING) != null) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onShiftClick(event: InventoryClickEvent) {
        // Cancel if event isn't a shift click
        if (!event.click.isShiftClick) {
            return
        }

        // Cancel if inventory is top inventory
        if (event.clickedInventory === event.view.topInventory) {
            return
        }

        // Cancel if no item in slot
        val itemStack = event.currentItem ?: return
        val itemMeta = itemStack.itemMeta
        val key = NamespacedKey(plugin, "persistent")
        if (itemMeta.persistentDataContainer.get(key, PersistentDataType.STRING) != null) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onDragToInventory(event: InventoryDragEvent) {
        val itemStack = event.oldCursor
        val itemMeta = itemStack.itemMeta
        val otherInv = event.view.topInventory
        val key = NamespacedKey(plugin, "persistent")
        if (itemMeta.persistentDataContainer.get(key, PersistentDataType.STRING) != null &&
            otherInv === event.inventory
        ) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onItemFrameUse(event: PlayerInteractEntityEvent) {
        if (event.rightClicked !is ItemFrame) {
            return
        }
        val key = NamespacedKey(plugin, "persistent")
        val mainHandItem = event.player.inventory.itemInMainHand
        val offHandItem = event.player.inventory.itemInOffHand

        // Cancel event if main hand has item
        val mainHandItemMeta = mainHandItem.itemMeta
        if (mainHandItemMeta != null) {
            if (mainHandItemMeta.persistentDataContainer.get(key, PersistentDataType.STRING) != null) {
                event.isCancelled = true
            }
        }

        // Cancel event if offhand has item and main hand is empty
        val offHandItemMeta = offHandItem.itemMeta
        if (offHandItemMeta != null) {
            if (offHandItemMeta.persistentDataContainer.get(key, PersistentDataType.STRING) != null &&
                mainHandItem.type == Material.AIR
            ) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val itemStack = event.itemInHand
        val itemMeta = itemStack.itemMeta
        val key = NamespacedKey(plugin, "persistent")
        if (itemMeta.persistentDataContainer.get(key, PersistentDataType.STRING) == null) {
            return
        }
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val droppedItems = event.drops
        val key = NamespacedKey(plugin, "persistent")
        for (droppedItem in droppedItems) {
            val itemMeta = droppedItem.itemMeta
            if (itemMeta.persistentDataContainer.get(key, PersistentDataType.STRING) != null) {
                droppedItems.remove(droppedItem)
            }
        }
    }
}
