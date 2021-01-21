package xyz.mizarc.persistentitems;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.mizarc.persistentitems.commands.PISubCommands.*;
import xyz.mizarc.persistentitems.commands.PersistentItemsCommand;
import xyz.mizarc.persistentitems.listeners.PlayerLoad;
import xyz.mizarc.persistentitems.listeners.PreventItemRemoval;
import xyz.mizarc.persistentitems.listeners.RunLinkedCommand;

import java.util.HashSet;
import java.util.Set;

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

        commandManager = new PaperCommandManager(this);
        registerCommandCompletions();
        registerCommands();
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

    private void registerCommandCompletions() {
        commandManager.getCommandCompletions().registerCompletion("pitems", context -> {
            Set<Item> items = itemContainer.getAllItems();
            Set<String> itemNames = new HashSet<>();
            for (Item item : items) {
                itemNames.add(item.getId());
            }
            return itemNames;
        });
    }

    private void registerCommands() {
        commandManager.registerDependency(ItemContainer.class, itemContainer);
        commandManager.registerDependency(ItemConfigIO.class, itemConfig);
        commandManager.registerCommand(new PersistentItemsCommand());
        commandManager.registerCommand(new AddCommand());
        commandManager.registerCommand(new RemoveCommand());
        commandManager.registerCommand(new AddCommandCommand());
        commandManager.registerCommand(new ActivateCommand());
        commandManager.registerCommand(new ShowCommand());
        commandManager.registerCommand(new HideCommand());
    }
}
