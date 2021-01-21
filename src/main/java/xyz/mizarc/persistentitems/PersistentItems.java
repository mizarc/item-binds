package xyz.mizarc.persistentitems;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.mizarc.persistentitems.commands.PISubCommands.HideCommand;
import xyz.mizarc.persistentitems.commands.PISubCommands.ShowCommand;
import xyz.mizarc.persistentitems.commands.PersistentItemsCommand;
import xyz.mizarc.persistentitems.listeners.PlayerLoad;
import xyz.mizarc.persistentitems.listeners.PreventItemRemoval;
import xyz.mizarc.persistentitems.listeners.RunLinkedCommand;

public class PersistentItems extends JavaPlugin {
    private FileConfiguration config = getConfig();
    private ItemConfigIO itemConfig;
    private ItemContainer itemContainer;
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        super.onEnable();

        itemConfig = new ItemConfigIO(this);
        itemContainer = new ItemContainer(this);
        itemContainer.loadActiveItems();

        // Listeners
        getServer().getPluginManager().registerEvents(new PlayerLoad(this), this);
        getServer().getPluginManager().registerEvents(new PreventItemRemoval(this), this);
        getServer().getPluginManager().registerEvents(new RunLinkedCommand(this), this);

        // Commands
        commandManager = new PaperCommandManager(this);
        commandManager.registerDependency(ItemContainer.class, itemContainer);
        commandManager.registerCommand(new PersistentItemsCommand());
        commandManager.registerCommand(new ShowCommand());
        commandManager.registerCommand(new HideCommand());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public ItemConfigIO getItemConfig() {
        return itemConfig;
    }

    public ItemContainer getItemContainer() {
        return itemContainer;
    }
}
