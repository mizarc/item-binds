package xyz.mizarc.persistentitems

import co.aikar.idb.DB.executeUpdate
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.sql.SQLException
import java.util.*

class PlayerItemsRepository(private val storage: DatabaseConnection) {
    private val playersItems: MutableMap<UUID, MutableSet<UUID>> = mutableMapOf()

    init {
        createTable()
        preload()
    }

    fun isPlayerHiddenItem(player: Player, item: Item): Boolean {
        return playersItems[player.uniqueId]?.contains(item.id) ?: false
    }

    fun getByPlayer(player: OfflinePlayer): Set<UUID> {
        return playersItems[player.uniqueId] ?: setOf()
    }

    fun add(player: Player, item: Item) {
        playersItems.getOrPut(player.uniqueId) { mutableSetOf() }.add(item.id)
        try {
            storage.connection.executeUpdate(
                "INSERT INTO playerItems (playerId, itemId) VALUES (?,?)", player.uniqueId, item.id)
        } catch (error: SQLException) {
            error.printStackTrace()
        }
    }

    fun remove(player: Player, item: Item) {
        val playerItems = playersItems[player.uniqueId] ?: return
        playerItems.remove(item.id)
        if (playerItems.isEmpty()) {
            playersItems.remove(player.uniqueId)
        }

        try {
        storage.connection.executeUpdate(
            "DELETE FROM playerItems WHERE playerId=? AND itemId=?",
            player.uniqueId, item.id)
        } catch (error: SQLException) {
            error.printStackTrace()
        }
    }

    private fun createTable() {
        try {
            storage.connection.executeUpdate(
                "CREATE TABLE IF NOT EXISTS playerItems (playerId TEXT, itemId TEXT);")
        } catch (error: SQLException) {
            error.printStackTrace()
        }
    }

    private fun preload() {
        val results = storage.connection.getResults("SELECT * FROM playerItems;")
        for (result in results) {
            playersItems.getOrPut(
                UUID.fromString(result.getString("playerId"))) { mutableSetOf() }
                .add(UUID.fromString(result.getString("itemId")))
        }
    }
}