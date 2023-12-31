package dev.mizarc.itembinds

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.persistence.PersistentDataType
import dev.mizarc.itembinds.utils.getStringMeta
import java.util.UUID

class PersistencyService(private val itemRepo: ItemRepository) {
    fun getItemsInInventory(inventory: PlayerInventory, itemId: UUID): List<ItemStack> {
        // Add each individual ItemStack with persistent metadata in inventory to list
        val presentItems: MutableList<ItemStack> = ArrayList()
        for (itemSlot in inventory.contents!!) {
            if (itemSlot == null) {
                continue
            }

            if (itemSlot.getStringMeta("item") == itemId.toString()) {
                presentItems.add(itemSlot)
            }
        }
        return presentItems
    }

    fun getSlotsWithItems(inventory: PlayerInventory, itemId: UUID): List<Int> {

        // Add each individual ItemStack with persistent metadata in inventory to list
        val presentItemSlots: MutableList<Int> = ArrayList()
        val inventoryContents = inventory.contents!!.clone()
        for (i in inventoryContents.indices) {
            if (inventoryContents[i] == null) {
                continue
            }

            val flag = inventoryContents[i]!!.getStringMeta("item") ?: continue
            if (flag == itemId.toString()) {
                presentItemSlots.add(i)
            }
        }
        return presentItemSlots
    }

    fun getActiveItems(): Set<Item> {
        val activeItems: MutableSet<Item> = mutableSetOf()
        for (item in itemRepo.getAll()) {
            if (item.isActive) {
                activeItems.add(item)
            }
        }
        return activeItems
    }
}