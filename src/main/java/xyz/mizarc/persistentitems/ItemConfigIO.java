package xyz.mizarc.persistentitems;

import org.bukkit.plugin.Plugin;

import java.io.File;

public class ItemConfigIO {
    private Plugin plugin;
    private File folder;

    public ItemConfigIO(Plugin plugin) {
        File folder = new File(plugin.getDataFolder(), "items");
        folder.mkdir();
    }

    public void addItem() {

    }

    public void removeItem() {

    }
}
