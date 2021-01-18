package xyz.mizarc.persistentitems;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.mizarc.persistentitems.commands.AddItemCommand;

public class PersistentItems extends JavaPlugin {
    public ItemConfigIO itemConfig;

    @Override
    public void onEnable() {
        super.onEnable();

        itemConfig = new ItemConfigIO(this);

        this.getCommand("add").setExecutor(new AddItemCommand());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
