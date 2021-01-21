package xyz.mizarc.persistentitems.commands.PISubCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import xyz.mizarc.persistentitems.Item;
import xyz.mizarc.persistentitems.ItemConfigIO;
import xyz.mizarc.persistentitems.ItemContainer;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("pi")
public class ActivateCommand extends BaseCommand {

    @Dependency
    Plugin plugin;

    @Dependency
    ItemConfigIO itemConfig;

    @Dependency
    ItemContainer itemContainer;

    @Subcommand("activate")
    public void onActivate(CommandSender sender, String itemId) {
        Item item = itemConfig.getItem(itemId);
        if (item == null) {
            sender.sendMessage("Item " + itemId + " does not exist");
            return;
        }

        if (!itemContainer.loadItem(item)) {
            sender.sendMessage("Item " + itemId + " is already loaded");
            return;
        }

        itemContainer.loadItem(itemConfig.getItem(itemId));
        itemConfig.setActive(itemId, true);
        giveAllItem(item.getItemStack(plugin), item.getSlot());
    }

    private void giveAllItem(ItemStack item, Integer slot) {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : players) {
            Inventory inventory = player.getInventory();
            if (inventory.getItem(slot) == null) {
                inventory.setItem(slot, item);
                continue;
            }
            inventory.addItem(item);
        }
    }
}
