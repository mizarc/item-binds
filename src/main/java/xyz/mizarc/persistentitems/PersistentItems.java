package xyz.mizarc.persistentitems;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.mizarc.persistentitems.commands.ActivateCommand;
import xyz.mizarc.persistentitems.commands.AddItemCommand;
import xyz.mizarc.persistentitems.commands.RemoveItemCommand;
import xyz.mizarc.persistentitems.listeners.PlayerLoad;

public class PersistentItems extends JavaPlugin {
    private FileConfiguration config = getConfig();
    private ItemConfigIO itemConfig;
    private ItemContainer itemContainer;

    @Override
    public void onEnable() {
        super.onEnable();

        itemConfig = new ItemConfigIO(this);
        itemContainer = new ItemContainer(this);
        itemContainer.loadActiveItems();

        // Listeners
        getServer().getPluginManager().registerEvents(new PlayerLoad(this), this);

        // Commands
        this.getCommand("additem").setExecutor(new AddItemCommand(this));
        this.getCommand("removeitem").setExecutor(new RemoveItemCommand(this));
        this.getCommand("activateitem").setExecutor(new ActivateCommand(this));
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
