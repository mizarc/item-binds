package dev.mizarc.itembinds.commands.itembinds

import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import dev.mizarc.itembinds.commands.ItemBindsCommand

@CommandAlias("persistentitems|pitems|pi")
class RemoveCmdCommand : ItemBindsCommand() {

    @Subcommand("removecmd")
    @CommandPermission("persistentitems.command.removecmd")
    @CommandCompletion("@pitems @nothing")
    @Syntax("<item> <command>")
    fun onRemoveCmd(sender: CommandSender, name: String, index: Int) {
        val item = itemRepo.getByName(name).firstOrNull()
        if (item == null) {
            sender.sendMessage("Item $name does not exist.")
            return
        }

        if (index > item.commands.size) {
            sender.sendMessage("There's no command at position $index.")
            return
        }

        val command = item.commands.removeAt(index)
        sender.sendMessage("Removed command at position $index.")
    }
}
