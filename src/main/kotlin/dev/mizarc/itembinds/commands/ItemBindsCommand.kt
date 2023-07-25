package dev.mizarc.itembinds.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import dev.mizarc.itembinds.ItemRepository
import dev.mizarc.itembinds.PersistencyService
import dev.mizarc.itembinds.PlayerItemsRepository

@CommandAlias("itembinds")
@CommandPermission("itembinds.command")
@Description("The base command for all Item Binds actions")
open class ItemBindsCommand: BaseCommand() {
    @Dependency protected lateinit var itemRepo: ItemRepository
    @Dependency protected lateinit var playerItemsRepo: PlayerItemsRepository
    @Dependency protected lateinit var persistencyService: PersistencyService

    @Default
    fun onPersistentItems(sender: CommandSender) {
        sender.sendMessage("Hi");
    }
}
