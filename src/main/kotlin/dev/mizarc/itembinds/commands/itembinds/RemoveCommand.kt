package dev.mizarc.itembinds.commands.itembinds

import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import dev.mizarc.itembinds.commands.ItemBindsCommand

@CommandAlias("itembinds")
class RemoveCommand : ItemBindsCommand() {
    @Subcommand("remove")
    @CommandPermission("itembinds.command.remove")
    @CommandCompletion("@pitems")
    @Syntax("<item>")
    fun onRemove(sender: CommandSender, name: String) {
        val item = itemRepo.getByName(name).firstOrNull()
        if (item == null) {
            sender.sendMessage("An item of that id doesn't exist.")
            return
        }

        itemRepo.remove(item)
        sender.sendMessage("Item $name has been removed.")
    }
}
