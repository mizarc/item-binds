package dev.mizarc.itembinds.commands.itembinds

import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import dev.mizarc.itembinds.commands.ItemBindsCommand

@CommandAlias("persistentitems|pitems|pi")
class AddCmdCommand : ItemBindsCommand() {

    @Subcommand("addcmd")
    @CommandPermission("persistentitems.command.addcommand")
    @CommandCompletion("@pitems @nothing")
    @Syntax("<item> <command>")
    fun onAddCmd(sender: CommandSender, name: String, commandArray: Array<String?>) {
        val item = itemRepo.getByName(name).firstOrNull()
        if (item == null) {
            sender.sendMessage("Item $name does not exist.")
            return
        }

        // Concatenate command to be assigned
        val itemCommand = commandArray.joinToString(separator = " ")

        // Add assigned command to item
        item.commands.add(itemCommand)
        itemRepo.update(item)
        sender.sendMessage("Command has been assigned to item $name")
    }
}
