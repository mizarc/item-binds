package dev.mizarc.itembinds.commands.itembinds

import co.aikar.commands.ConditionFailedException
import co.aikar.commands.MessageKeys
import co.aikar.commands.annotation.*
import co.aikar.commands.annotation.Optional
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory
import dev.mizarc.itembinds.Item
import dev.mizarc.itembinds.commands.ItemBindsCommand
import java.util.*

@CommandAlias("itembinds")
class HideCommand : ItemBindsCommand() {

    @Subcommand("hide")
    @CommandPermission("itembinds.command.hide")
    @CommandCompletion("@pitems @players")
    @Syntax("<item> [player]")
    fun onHide(sender: CommandSender, name: String) {
        // Error if persistent item is not active
        val item = itemRepo.getByName(name).firstOrNull()
        if (item == null) {
            sender.sendMessage("Item $sender does not exist")
            return
        }

        // Error if console is trying to use this without specifying a player
        if (sender !is Player) {
            sender.sendMessage("You must specify the player argument as the console")
            return
        }

        // Remove item from own inventory unless you don't have it
        if (!removeFromInventory(sender.inventory, item)) {
            sender.sendMessage("Item ${item.name} is not in your inventory")
            return
        }
        playerItemsRepo.add(sender, item)
        sender.sendMessage("Item ${item.name} has been removed from your inventory")
    }

    private fun onHideOthers(sender: CommandSender, item: Item, player: Player) {
        // Error if player doesn't have the 'others' permission
        if (!sender.hasPermission("persistentitems.command.hide.others")) {
            throw ConditionFailedException(MessageKeys.PERMISSION_DENIED_PARAMETER)
        }

        // Remove item from specified player's inventory unless player doesn't have it
        if (!removeFromInventory(player.inventory, item)) {
            sender.sendMessage("Item ${item.name} is not in ${player.displayName()}'s inventory")
            return
        }

        playerItemsRepo.add(player, item)
        sender.sendMessage("Item ${item.name} has been removed from ${player.displayName()}'s inventory")
    }

    private fun removeFromInventory(inventory: PlayerInventory, item: Item): Boolean {
        val itemSlots: List<Int> = persistencyService.getSlotsWithItems(inventory, item.id) ?: return false
        if (itemSlots.isEmpty()) {
            return false
        }

        // Remove instances of persistent items from inventory
        for (slot in itemSlots) {
            inventory.setItem(slot, null)
        }
        return true
    }
}
