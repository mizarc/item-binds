package xyz.mizarc.persistentitems

import org.bukkit.plugin.Plugin

class Config(plugin: Plugin) {

    init {
        plugin.config.addDefault("temp", 1)
        plugin.config.options().copyDefaults(true)
        plugin.saveConfig()
    }
}