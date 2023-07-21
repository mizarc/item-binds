package dev.mizarc.itembinds.commands.persistentitems

import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import dev.mizarc.itembinds.commands.PersistentItemsCommand

@CommandAlias("persistentitems|pitems|pi")
class AddCmdCommand : PersistentItemsCommand() {

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
        val stringBuilder = StringBuilder()
        for (commandWord in commandArray) {
            stringBuilder.append(commandWord)
        }
        val itemCommand = stringBuilder.toString()

        // Add assigned command to item
        item.commands.add(itemCommand)
        itemRepo.update(item)
        sender.sendMessage("Command has been assigned to item $name")
    }
}
