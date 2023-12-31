package dev.mizarc.itembinds;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import dev.mizarc.itembinds.commands.ItemBindsCommand;
import dev.mizarc.itembinds.commands.itembinds.*
import dev.mizarc.itembinds.listeners.ItemRemovalListener
import dev.mizarc.itembinds.listeners.ItemUseListener;
import dev.mizarc.itembinds.listeners.PlayerLoadListener
import java.io.File

import java.util.HashSet;
import java.util.Locale;

class ItemBinds: JavaPlugin() {
    private val config = Config(this)
    private lateinit var commandManager: PaperCommandManager
    private val databaseConnection = DatabaseConnection(this)
    private val itemRepo = ItemRepository("$dataFolder/items")
    private val playerItemsRepo = PlayerItemsRepository(databaseConnection)
    private val persistencyService = PersistencyService(itemRepo)

    override fun onEnable() {
        super.onEnable()

        File("$dataFolder/items").mkdir()
        commandManager = PaperCommandManager(this)
        registerLocales()
        transferResources()
        registerDependencies()
        registerCommandCompletions()
        registerCommands()
        registerListeners()
        logger.info("Item Binds has been enabled.")
    }

    override fun onDisable() {
        super.onDisable()
        logger.info("Item Binds has been disabled.")
    }

    private fun registerLocales() {
        try {
            commandManager.locales.loadYamlLanguageFile("lang/en_US.yml", Locale.ENGLISH)
        }
        catch (error: Exception) {
            error.printStackTrace()
        }
    }

    private fun transferResources() {
        saveResource("lang/en_US.yml", false);
    }

    private fun registerDependencies() {
        commandManager.registerDependency(DatabaseConnection::class.java, databaseConnection)
        commandManager.registerDependency(ItemRepository::class.java, itemRepo)
        commandManager.registerDependency(PlayerItemsRepository::class.java, playerItemsRepo)
        commandManager.registerDependency(PersistencyService::class.java, persistencyService)
    }

    private fun registerCommandCompletions() {
        commandManager.commandCompletions.registerCompletion("pitems") { _ ->
            val items = persistencyService.getActiveItems()
            val itemNames = HashSet<String>()
            for (item in items) {
                itemNames.add(item.name)
            }
            itemNames
        }
    }

    private fun registerCommands() {
        commandManager.registerCommand(ItemBindsCommand())
        commandManager.registerCommand(AddCommand())
        commandManager.registerCommand(RemoveCommand())
        commandManager.registerCommand(AddCmdCommand())
        commandManager.registerCommand(ActivateCommand())
        commandManager.registerCommand(ShowCommand())
        commandManager.registerCommand(HideCommand())
    }

    private fun registerListeners() {
        server.pluginManager.registerEvents(PlayerLoadListener(playerItemsRepo, persistencyService), this);
        server.pluginManager.registerEvents(ItemRemovalListener(this), this);
        server.pluginManager.registerEvents(ItemUseListener(itemRepo), this);
    }
}
