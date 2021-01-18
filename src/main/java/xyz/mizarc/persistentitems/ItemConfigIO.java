package xyz.mizarc.persistentitems;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ItemConfigIO {
    private Plugin plugin;
    private File folder;
    private File config;

    public ItemConfigIO(Plugin plugin) {
        folder = new File(plugin.getDataFolder(), "items");
        folder.mkdir();
    }

    public void addItem(String id) {
        File file = new File(folder, id + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeItem() {

    }
}
