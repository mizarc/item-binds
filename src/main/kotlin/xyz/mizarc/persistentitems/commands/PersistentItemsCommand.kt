package xyz.mizarc.persistentitems.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import xyz.mizarc.persistentitems.ItemRepository
import xyz.mizarc.persistentitems.PersistencyService
import xyz.mizarc.persistentitems.PlayerItemsRepository

@CommandAlias("persistentitems|pitems|pi")
@CommandPermission("persistentitems.command")
@Description("The base command for all Persistent Item actions")
open class PersistentItemsCommand: BaseCommand() {
    @Dependency protected lateinit var itemRepo: ItemRepository
    @Dependency protected lateinit var playerItemsRepo: PlayerItemsRepository
    @Dependency protected lateinit var persistencyService: PersistencyService

    @Default
    fun onPersistentItems(sender: CommandSender) {
        sender.sendMessage("Hi");
    }
}
