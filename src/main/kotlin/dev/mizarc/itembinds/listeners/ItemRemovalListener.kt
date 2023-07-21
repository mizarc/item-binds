package dev.mizarc.itembinds.listeners

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
import dev.mizarc.itembinds.ItemBinds
import dev.mizarc.itembinds.utils.getStringMeta

class ItemRemovalListener(private val plugin: ItemBinds) : Listener {
    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        val itemStack = event.itemDrop.itemStack
        if (itemStack.getStringMeta("item") != null) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onMoveToInventory(event: InventoryClickEvent) {
        if (event.cursor == null) {
            return
        }

        // Cancel if item is in bottom
        if (event.clickedInventory === event.view.bottomInventory) {
            return
        }

        // Cancel if item meta doesn't exist
        val itemStack = event.cursor ?: return
        if (itemStack.getStringMeta("item") != null) {
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
        if (itemStack.getStringMeta("item") != null) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onDragToInventory(event: InventoryDragEvent) {
        val itemStack = event.oldCursor
        val otherInv = event.view.topInventory
        if (itemStack.getStringMeta("item") != null && otherInv === event.inventory) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onItemFrameUse(event: PlayerInteractEntityEvent) {
        if (event.rightClicked !is ItemFrame) {
            return
        }
        val mainHandItem = event.player.inventory.itemInMainHand
        val offHandItem = event.player.inventory.itemInOffHand

        // Cancel event if main hand has item
        if (mainHandItem.getStringMeta("item") != null) {
            event.isCancelled = true
        }

        // Cancel event if offhand has item and main hand is empty
        if (offHandItem.getStringMeta("item") != null &&
            mainHandItem.type == Material.AIR
        ) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val itemStack = event.itemInHand
        if (itemStack.getStringMeta("item") == null) {
            return
        }
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val droppedItems = event.drops
        for (droppedItem in droppedItems) {
            if (droppedItem.getStringMeta("item") != null) {
                droppedItems.remove(droppedItem)
            }
        }
    }
}
