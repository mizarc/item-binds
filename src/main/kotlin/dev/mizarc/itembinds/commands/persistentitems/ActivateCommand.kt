package dev.mizarc.itembinds.commands.persistentitems;

import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import dev.mizarc.itembinds.commands.PersistentItemsCommand

@CommandAlias("persistentitems|pitems|pi")
class ActivateCommand: PersistentItemsCommand() {
    @Subcommand("activate")
    @CommandPermission("persistentitems.command.activate")
    @CommandCompletion("@pitems")
    @Syntax("<item>")
    fun onActivate(sender: CommandSender, name: String) {
        val item = itemRepo.getByName(name).firstOrNull()
        if (item == null) {
            sender.sendMessage("Item $name does not exist")
            return
        }

        if (item.isActive) {
            sender.sendMessage("Item $name is already loaded")
            return
        }

        item.isActive = true
        itemRepo.update(item)
        giveAllItem(item.itemStack, item.slot)
    }

    private fun giveAllItem(item: ItemStack, slot: Int) {
        for (player in Bukkit.getOnlinePlayers()) {
            val inventory = player.inventory
            if (inventory.getItem(slot) == null) {
                inventory.setItem(slot, item)
                continue
            }
            inventory.addItem(item)
        }
    }
}
