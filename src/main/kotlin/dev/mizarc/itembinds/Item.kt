package dev.mizarc.itembinds


import org.bukkit.inventory.ItemStack
import dev.mizarc.itembinds.utils.setStringMeta


import java.util.UUID

class Item(val id: UUID, var name: String, itemStack: ItemStack, var slot: Int,
           var commands: MutableList<String>, var isActive: Boolean) {
    var itemStack = itemStack
        get() { return field.setStringMeta("item", id.toString()) }

    var unmodifiedItemStack = itemStack

    constructor(name: String, itemStack: ItemStack, slot: Int):
            this(UUID.randomUUID(), name, itemStack, slot, mutableListOf(), false)
}
