package com.aregcraft.reforging.command;

import com.aregcraft.delta.api.FormattingContext;
import com.aregcraft.delta.api.InjectPlugin;
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
        if (args.size() == 0) {
            showUsage(sender);
            return true;
        }
        var subcommand = args.get(0);
        if (args.size() == 1 && subcommand.equals("reforges")) {
            listReforges(sender);
            return true;
        }
        if (args.size() != 2) {
            showUsage(sender);
            return true;
        }
        switch (subcommand) {
            case "reforge" -> showReforge(sender, args.get(1));
            case "recipe" -> showRecipe(sender, args.get(1));
            default -> showUsage(sender);
        }
        return true;
    }

    private void listReforges(Player player) {
        sendMessage(player, "%aqua%"
                + String.join("%gray%, %aqua%", plugin.getReforges().getIds()));
    }

    private void showReforge(Player player, String id) {
        var reforge = plugin.getReforges().findAny(id);
        if (reforge == null) {
            showUsage(player);
            return;
        }
        var inventory = Bukkit.createInventory(player, 27, getTitle(reforge));
        var stone = plugin.getStones().findAny(id);
        var exampleWeapon = getExampleWeapon(player, reforge).unwrap();
        if (stone == null) {
            inventory.setItem(11, exampleWeapon);
            inventory.setItem(13, new ItemStack(Material.DIAMOND, plugin.getReforgingAnvil().getPrice()));
            inventory.setItem(15, getChance(id).unwrap());
        } else {
            inventory.setItem(12, exampleWeapon);
            inventory.setItem(14, stone.getItem().unwrap());
        }
        inventories.add(inventory);
        player.openInventory(inventory);
    }

    private String getTitle(Reforge reforge) {
        return FormattingContext.withPlugin(plugin).format(reforge.getName());
    }

    private ItemWrapper getChance(String id) {
        return ItemWrapper.builder()
                .material(Material.RABBIT_FOOT)
                .name("%green%" + DECIMAL_FORMAT.format(plugin.getReforgeChance(id)) + "%")
                .build();
    }

    private ItemWrapper getExampleWeapon(Player player, Reforge reforge) {
        return reforge.apply(player, ItemWrapper.withMaterial(getExampleMaterial(reforge)), plugin);
    }

    private Material getExampleMaterial(Reforge reforge) {
        return Material.valueOf("DIAMOND_" + reforge.getTargets().iterator().next());
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
        var stone = plugin.getStones().findAny(id);
        return stone == null ? null : stone.getRecipe();
    }

    private String getTitle(String id) {
        return id.equals("REFORGING_ANVIL")
                ? plugin.getReforgingAnvil().getItem().getName()
                : plugin.getStones().findAny(id).getItem().getName();
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        if (inventories.contains(event.getInventory())) {
            event.setCancelled(true);
        }
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
            case "reforge" -> new ArrayList<>(plugin.getReforges().getIds());
            case "recipe" -> getRecipes();
            default -> null;
        };
    }

    private List<String> getRecipes() {
        var recipes = new ArrayList<>(plugin.getStones().getIds());
        recipes.add("REFORGING_ANVIL");
        return recipes;
    }

    private void showUsage(Player sender) {
        sendMessage(sender, plugin.getReforgingInfoUsage());
    }

    private void sendMessage(Player player, String... messages) {
        player.sendMessage(Arrays.stream(messages)
                .map(FormattingContext.withPlugin(plugin)::format)
                .toArray(String[]::new));
    }
}
