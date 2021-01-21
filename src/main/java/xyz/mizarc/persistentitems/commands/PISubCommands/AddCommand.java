package xyz.mizarc.persistentitems.commands.PISubCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.mizarc.persistentitems.ItemConfigIO;

@CommandAlias("persistentitems|pitems|pi")
public class AddCommand extends BaseCommand {

    @Dependency
    ItemConfigIO itemConfig;

    @Subcommand("add")
    @CommandPermission("persistentitems.command.add")
    public void onAdd(Player player, String itemId, int slot) {
        ItemStack heldItemStack = player.getInventory().getItemInMainHand();
        itemConfig.addItem(itemId, heldItemStack, slot);
        player.sendMessage("Held item " + heldItemStack.getItemMeta().getDisplayName() + " has been added");
    }
}
