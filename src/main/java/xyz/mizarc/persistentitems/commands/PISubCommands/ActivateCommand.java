package xyz.mizarc.persistentitems.commands.PISubCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.mizarc.persistentitems.Item;
import xyz.mizarc.persistentitems.ItemConfigIO;
import xyz.mizarc.persistentitems.ItemContainer;
import xyz.mizarc.persistentitems.PersistentItems;
import xyz.mizarc.persistentitems.commands.SubCommand;

import java.util.ArrayList;
import java.util.List;

public class ActivateCommand implements SubCommand {
    PersistentItems plugin;

    public ActivateCommand(PersistentItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("No arguments specified");
            return false;
        }

        Item item = plugin.getItemConfig().getItem(args[0]);
        if (item == null) {
            return true;
        }

        ItemContainer itemContainer = plugin.getItemContainer();
        ItemConfigIO itemConfig = plugin.getItemConfig();
        itemContainer.loadItem(itemConfig.getItem(args[0]));
        itemConfig.setActive(args[0], true);
        giveActivatedItem(itemContainer.getItem(args[0]).getItemStack(plugin),
                itemContainer.getItem(args[0]).getSlot());
        return true;
    }

    private void giveActivatedItem(ItemStack item, Integer slot) {
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