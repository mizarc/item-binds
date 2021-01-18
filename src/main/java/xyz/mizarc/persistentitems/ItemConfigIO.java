package xyz.mizarc.persistentitems;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
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

    public void addItem(String id, ItemStack item, Integer slot) {
        File file = new File(folder, id + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("item", item.getType().getKey().toString());
        config.set("slot", slot);
        config.set("name", item.getItemMeta().getDisplayName());
        if (item.getLore() != null) {
            config.set("lore", item.getLore());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeItem() {

    }
}
