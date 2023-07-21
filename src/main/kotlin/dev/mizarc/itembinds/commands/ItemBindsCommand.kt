package dev.mizarc.itembinds.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import dev.mizarc.itembinds.ItemRepository
import dev.mizarc.itembinds.PersistencyService
import dev.mizarc.itembinds.PlayerItemsRepository

@CommandAlias("persistentitems|pitems|pi")
@CommandPermission("persistentitems.command")
@Description("The base command for all Persistent Item actions")
open class ItemBindsCommand: BaseCommand() {
    @Dependency protected lateinit var itemRepo: ItemRepository
    @Dependency protected lateinit var playerItemsRepo: PlayerItemsRepository
    @Dependency protected lateinit var persistencyService: PersistencyService

    @Default
    fun onPersistentItems(sender: CommandSender) {
        sender.sendMessage("Hi");
    }
}
