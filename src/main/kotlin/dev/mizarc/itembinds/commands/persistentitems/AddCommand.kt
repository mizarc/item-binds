package dev.mizarc.itembinds.commands.persistentitems

import co.aikar.commands.annotation.*
import org.bukkit.entity.Player
import dev.mizarc.itembinds.Item
import dev.mizarc.itembinds.commands.PersistentItemsCommand

@CommandAlias("persistentitems|pitems|pi")
class AddCommand : PersistentItemsCommand() {
    @Subcommand("add")
    @CommandPermission("persistentitems.command.add")
    @CommandCompletion("@nothing @nothing")
    @Syntax("<item> <slot>")
    fun onAdd(player: Player, name: String, slot: Int) {
        val heldItemStack = player.inventory.itemInMainHand
        itemRepo.add(Item(name, heldItemStack, slot))
    }
}
