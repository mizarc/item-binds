package xyz.mizarc.persistentitems.listeners

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.Inventory
import org.bukkit.persistence.PersistentDataType
import xyz.mizarc.persistentitems.Item
import xyz.mizarc.persistentitems.PersistencyService
import xyz.mizarc.persistentitems.PlayerItemsRepository

class PlayerLoadListener(private val playerItemsRepository: PlayerItemsRepository,
                         private val persistencyService: PersistencyService) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        giveItemsIfMissing(event.player)
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        giveItemsIfMissing(event.player)
    }

    private fun giveItemsIfMissing(player: Player) {
        val inventory: Inventory = player.inventory
        val activeItems: Set<Item> = persistencyService.getActiveItems()

        // Loop through all active items
        for (activeItem in activeItems) {
            // Cancel if item is on player's ignore list
            if (playerItemsRepository.isPlayerHiddenItem(player, activeItem)) break

            // Scan inventory for persistent item
            if (checkInventoryForItem(inventory, activeItem)) {
                break
            }
            giveItem(player, activeItem)
        }
    }

    private fun giveItem(player: Player, item: Item) {
        val inventory: Inventory = player.inventory
        if (inventory.getItem(item.slot) == null) {
            inventory.setItem(item.slot, item.itemStack)
            return
        }
        inventory.addItem(item.itemStack)
        Bukkit.getLogger().info("what ${item.itemStack}")
    }

    private fun checkInventoryForItem(inventory: Inventory, item: Item): Boolean {
        for (itemStack in inventory) {
            if (itemStack == null) {
                continue
            }

            // Go to next item if this item doesn't have metadata (probably air)
            val itemMeta = itemStack.itemMeta ?: continue

            // Skip item if it doesn't have the special key
            val key = NamespacedKey("persistentitems", "item")
            if (itemMeta.persistentDataContainer.get(key, PersistentDataType.STRING) == null) {
                continue
            }

            // Go to next item if player already has item
            if (itemMeta.persistentDataContainer.get(key, PersistentDataType.STRING) == item.id.toString()) {
                return true
            }
        }
        return false
    }
}
