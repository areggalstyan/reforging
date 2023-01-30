package com.aregcraft.reforging.command;

import com.aregcraft.delta.api.FormattingContext;
import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.PersistentDataWrapper;
import com.aregcraft.delta.api.Recipe;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.reforging.Reforge;
import com.aregcraft.reforging.Reforging;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RegisteredCommand("reforginginfo")
public class ReforgingInfoCommand implements CommandWrapper, Listener {
    private static final List<String> SUBCOMMANDS = List.of("reforges", "reforge", "recipe");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    private final List<Inventory> inventories = new ArrayList<>();
    @InjectPlugin
    private Reforging plugin;

    @Override
    public boolean execute(Player sender, List<String> args) {
        if (args.size() == 1) {
            listReforges(sender);
            return true;
        }
        if (args.size() != 2) {
            showUsage(sender);
            return true;
        }
        switch (args.get(0)) {
            case "reforge" -> showReforge(sender, args.get(1));
            case "recipe" -> showRecipe(sender, args.get(1));
            default -> showUsage(sender);
        }
        return true;
    }

    private void listReforges(Player player) {
        sendMessage(player, "%aqua%" + String.join("%gray%, %aqua%", plugin.getReforgeIds()));
    }

    private void showReforge(Player player, String id) {
        var reforge = plugin.getReforge(id);
        if (reforge == null) {
            showUsage(player);
            return;
        }
        var inventory = Bukkit.createInventory(player, 27, getTitle(reforge));
        var stone = plugin.getReforgeStone(id);
        var exampleWeapon = getExampleWeapon(player, reforge).unwrap();
        if (stone == null) {
            inventory.setItem(11, exampleWeapon);
            inventory.setItem(13, new ItemStack(Material.DIAMOND, plugin.getReforgingAnvil().getPrice()));
            inventory.setItem(15, getChance(id).unwrap());
        } else {
            inventory.setItem(12, exampleWeapon);
            inventory.setItem(14, stone.getItem().unwrap());
        }
        PersistentDataWrapper.wrap(plugin, player).set("noninteractive_inventory", true);
        inventories.add(inventory);
        player.openInventory(inventory);
    }

    private String getTitle(Reforge reforge) {
        return FormattingContext.DEFAULT.format(reforge.getName());
    }

    private ItemWrapper getChance(String id) {
        return ItemWrapper.builder()
                .material(Material.RABBIT_FOOT)
                .name("%green%" + DECIMAL_FORMAT.format(plugin.getReforgeChance(id)) + "%")
                .build();
    }

    private ItemWrapper getExampleWeapon(Player player, Reforge reforge) {
        return reforge.apply(player, ItemWrapper.withMaterial(Material.DIAMOND_SWORD), plugin);
    }

    private void showRecipe(Player player, String id) {
        var recipe = getRecipe(id);
        if (recipe == null) {
            showUsage(player);
            return;
        }
        var inventory = Bukkit.createInventory(player, 45, getTitle(id));
        for (var i = 0; i < 9; i++) {
            inventory.setItem(i + i / 3 * 6 + 12, new ItemStack(recipe.get(i)));
        }
        inventories.add(inventory);
        player.openInventory(inventory);
    }

    private Recipe getRecipe(String id) {
        if (id.equals("REFORGING_ANVIL")) {
            return plugin.getReforgingAnvil().getRecipe();
        }
        var stone = plugin.getReforgeStone(id);
        return stone == null ? null : stone.getRecipe();
    }

    private String getTitle(String id) {
        return id.equals("REFORGING_ANVIL")
                ? plugin.getReforgingAnvil().getItem().getName()
                : plugin.getReforgeStone(id).getItem().getName();
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        event.setCancelled(inventories.contains(event.getInventory()));
    }

    @Override
    public List<String> suggest(Player sender, List<String> args) {
        if (args.size() == 1) {
            return SUBCOMMANDS;
        }
        if (args.size() != 2) {
            return null;
        }
        return switch (args.get(0)) {
            case "reforge" -> plugin.getReforgeIds();
            case "recipe" -> getRecipes();
            default -> null;
        };
    }

    private List<String> getRecipes() {
        var recipes = new ArrayList<>(plugin.getReforgeStoneIds());
        recipes.add("REFORGING_ANVIL");
        return recipes;
    }

    private void showUsage(Player sender) {
        sendMessage(sender, """
                %red%/reforginginfo reforges %gray%- %yellow%List all reforges
                %red%/reforginginfo reforge <id> %gray%- %yellow%Show info about a reforge
                %red%/reforginginfo recipe (REFORGING_ANVIL|<stone>) %gray%- %yellow%Show a crafting recipe""");
    }

    private void sendMessage(Player player, String... messages) {
        player.sendMessage(Arrays.stream(messages).map(FormattingContext.DEFAULT::format).toArray(String[]::new));
    }
}
