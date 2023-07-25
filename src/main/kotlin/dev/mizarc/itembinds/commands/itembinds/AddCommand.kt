package dev.mizarc.itembinds.commands.itembinds

import co.aikar.commands.annotation.*
import org.bukkit.entity.Player
import dev.mizarc.itembinds.Item
import dev.mizarc.itembinds.commands.ItemBindsCommand

@CommandAlias("itembinds")
class AddCommand : ItemBindsCommand() {
    @Subcommand("add")
    @CommandPermission("itembinds.command.add")
    @CommandCompletion("@nothing @nothing")
    @Syntax("<item> <slot>")
    fun onAdd(player: Player, name: String, slot: Int) {
        val heldItemStack = player.inventory.itemInMainHand
        itemRepo.add(Item(name, heldItemStack, slot))
    }
}
