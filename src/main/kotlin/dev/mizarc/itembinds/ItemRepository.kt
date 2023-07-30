package dev.mizarc.itembinds

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

class ItemRepository(private val folderName: String) {
    private val folder = File(folderName)
    private val items: MutableMap<UUID, Item> = mutableMapOf()

    init {
        preload()
    }

    fun getAll(): Set<Item> {
        return items.values.toSet()
    }

    fun getById(id: UUID): Item? {
        return items.values.firstOrNull { it.id == id }
    }

    fun getByName(name: String): List<Item> {
        return items.values.filter { it.name == name }
    }

    fun add(item: Item) {
        items[item.id] = item

        val file = File(folder, "${item.id}.yml")
        file.createNewFile()

        val config = YamlConfiguration.loadConfiguration(file)
        config.set("id", item.id.toString())
        config.set("name", item.name)
        config.set("item", item.unmodifiedItemStack)
        config.set("slot", item.slot)
        config.set("commands", item.commands)
        config.set("active", item.isActive)
        config.save(file)
    }

    fun update(item: Item) {
        items[item.id] = item

        val file = File(folder, "${item.id}.yml")
        if (!file.exists()) return

        val config = YamlConfiguration.loadConfiguration(file)
        config.set("name", item.name)
        config.set("item", item.itemStack)
        config.set("slot", item.slot)
        config.set("commands", item.commands)
        config.set("active", item.isActive)
        config.save(file)
    }

    fun remove(item: Item) {
        items.remove(item.id)

        val file = File(folder, "${item.id}.yml")
        if (!file.exists()) return
        file.delete()
    }

    private fun preload() {
        val folder = File(folderName)
        val files = folder.listFiles() ?: return
        for (file in files) {
            val config: FileConfiguration = YamlConfiguration.loadConfiguration(file)
            val name = config.getString("name") ?: continue
            val itemStack = config.getItemStack("item") ?: continue
            val item = Item(UUID.fromString(config.getString("id")),
                name,
                itemStack,
                config.getInt("slot"),
                config.getStringList("commands"),
                config.getBoolean("active"))
            items[UUID.fromString(config.getString("id"))] = item
        }
    }
}