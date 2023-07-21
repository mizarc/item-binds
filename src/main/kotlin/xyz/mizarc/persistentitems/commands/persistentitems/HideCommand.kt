package xyz.mizarc.persistentitems.commands.persistentitems

import co.aikar.commands.ConditionFailedException
import co.aikar.commands.MessageKeys
import co.aikar.commands.annotation.*
import co.aikar.commands.annotation.Optional
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory
import xyz.mizarc.persistentitems.Item
import xyz.mizarc.persistentitems.commands.PersistentItemsCommand
import java.util.*

@CommandAlias("persistentitems|pitems|pi")
class HideCommand : PersistentItemsCommand() {

    @Subcommand("hide")
    @CommandPermission("persistentitems.command.hide")
    @CommandCompletion("@pitems @players")
    @Syntax("<item> [player]")
    fun onHide(sender: CommandSender, name: String, @Optional player: Player) {
        // Error if persistent item is not active
        val item = itemRepo.getByName(name).firstOrNull()
        if (item == null) {
            sender.sendMessage("Item $sender does not exist")
            return
        }

        // Forward command to 'others' version if player argument is specified
        if (player != null) {
            onHideOthers(sender, item, player)
            return
        }

        // Error if console is trying to use this without specifying a player
        if (sender !is Player) {
            sender.sendMessage("You must specify the player argument as the console")
            return
        }

        // Remove item from own inventory unless you don't have it
        val selfPlayer = sender
        if (!removeFromInventory(selfPlayer.inventory, item)) {
            sender.sendMessage("Item ${item.id} is not in your inventory")
            return
        }
        playerItemsRepo.add(selfPlayer, item)
        sender.sendMessage("Item ${item.id} has been removed from your inventory")
    }

    private fun onHideOthers(sender: CommandSender, item: Item, player: Player) {
        // Error if player doesn't have the 'others' permission
        if (!sender.hasPermission("persistentitems.command.hide.others")) {
            throw ConditionFailedException(MessageKeys.PERMISSION_DENIED_PARAMETER)
        }

        // Remove item from specified player's inventory unless player doesn't have it
        if (!removeFromInventory(player.inventory, item)) {
            sender.sendMessage("Item ${item.id} is not in ${player.displayName()}'s inventory")
            return
        }

        playerItemsRepo.add(player, item)
        sender.sendMessage("Item ${item.id} has been removed from ${player.displayName()}'s inventory")
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
