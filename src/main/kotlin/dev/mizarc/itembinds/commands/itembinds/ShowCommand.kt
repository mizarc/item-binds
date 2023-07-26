package dev.mizarc.itembinds.commands.itembinds;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.annotation.*;
import co.aikar.commands.annotation.Optional
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import dev.mizarc.itembinds.Item;
import dev.mizarc.itembinds.commands.ItemBindsCommand;
import java.util.*

@CommandAlias("itembinds")
class ShowCommand: ItemBindsCommand() {

    @Subcommand("show")
    @CommandPermission("itembinds.command.show")
    @CommandCompletion("@pitems @players")
    @Syntax("<item> [player]")
    fun onShow(sender: CommandSender, name: String) {
        val item = itemRepo.getByName(name).firstOrNull()

        // Error if persistent item is not active
        if (item == null) {
            sender.sendMessage("Item $name does not exist");
            return;
        }

        // Error if console is trying to use this without specifying a player
        if (sender !is Player) {
            sender.sendMessage("You must specify the player argument as the console");
            return;
        }

        //Add item to own inventory unless you already have it
        if (!addToInventory(sender.inventory, item.id)) {
            sender.sendMessage("Item ${item.name} is already in your inventory");
            return;
        }

        playerItemsRepo.remove(sender, item)
        sender.sendMessage("Item ${item.name} has been added to your inventory");
    }

    fun onShowOthers(sender: CommandSender, item: Item, player: Player) {
        // Error if player doesn't have the 'others' permission
        if (!sender.hasPermission("persistentitems.command.show.others")) {
            throw ConditionFailedException(MessageKeys.PERMISSION_DENIED_PARAMETER);
        }

        // Add item to specified player's inventory unless player already has it
        if (!addToInventory(player.inventory, item.id)) {
            sender.sendMessage("Item ${item.name} is already in ${player.displayName()}'s inventory")
            return;
        }
        playerItemsRepo.remove(player, item)
        sender.sendMessage("Item ${item.name} has been added to ${player.displayName()}'s inventory")
    }

    private fun addToInventory(inventory: PlayerInventory, itemId: UUID): Boolean {
        val presentItems: List<ItemStack> = persistencyService.getItemsInInventory(inventory, itemId);
        if (presentItems.isNotEmpty()) {
            return false;
        }

        // True if the inventory doesn't have the item and attempt to add the item to the specified slot.
        // Add to any slot if that slot is occupied
        val item = itemRepo.getById(itemId) ?: return false
        if (inventory.getItem(item.slot) == null) {
            inventory.setItem(item.slot, item.itemStack);
            return true
        }
        inventory.addItem(item.itemStack);
        return true
    }
}
