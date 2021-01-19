package xyz.mizarc.persistentitems;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ItemConfigIO {
    private Plugin plugin;
    private File folder;
    private File config;

    public ItemConfigIO(Plugin plugin) {
        this.plugin = plugin;
        folder = new File(plugin.getDataFolder(), "items");
        folder.mkdir();
    }

    public Set<Item> getActiveItems() {
        Set<Item> activeItems = new HashSet<>();
        String[] fileNames = folder.list();
        for (String fileName : fileNames) {
            Item item = getItemIfActive(fileName);
            activeItems.add(item);
        }
        return activeItems;
    }

    public Item getItem(String id) {
        File file = new File(folder, id + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        ItemStack itemStack = new ItemStack(Material.getMaterial(config.getString("id")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(config.getString("name"));
        itemMeta.setLore(config.getStringList("lore"));
        itemStack.setItemMeta(itemMeta);

        return new Item(itemStack, config.getInt("slot"), config.getStringList("commands"));
    }

    public Item getItemIfActive(String id) {
        File file = new File(folder, id + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (!config.getBoolean("active")) {
            return null;
        }

        ItemStack itemStack = new ItemStack(Material.getMaterial(config.getString("id")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(config.getString("name"));
        itemMeta.setLore(config.getStringList("lore"));
        itemStack.setItemMeta(itemMeta);

        return new Item(itemStack, config.getInt("slot"), config.getStringList("commands"));
    }

    public void addItem(String id, ItemStack item, Integer slot) {
        File file = new File(folder, id + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("item", item.getType().getKey().toString());
        config.set("active", false);
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

    public boolean removeItem(String id) {
        File file = new File(folder, id + ".yml");

        boolean deleteResult = file.delete();
        return deleteResult;
    }
}
