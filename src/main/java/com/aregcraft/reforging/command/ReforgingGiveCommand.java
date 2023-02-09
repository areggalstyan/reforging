package com.aregcraft.reforging.command;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.reforging.Reforging;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RegisteredCommand("reforginggive")
public class ReforgingGiveCommand implements CommandWrapper {
    @InjectPlugin
    private Reforging plugin;

    @Override
    public boolean execute(Player sender, List<String> args) {
        if (args.size() != 1) {
            return false;
        }
        var inventory = sender.getInventory();
        var id = args.get(0);
        if (id.equals("REFORGING_ANVIL")) {
            inventory.addItem(plugin.getReforgingAnvil().getItem().unwrap());
            return true;
        }
        var stone = plugin.getStones().findAny(id);
        if (stone == null) {
            return false;
        }
        inventory.addItem(stone.getItem().unwrap());
        return true;
    }

    @Override
    public List<String> suggest(Player sender, List<String> args) {
        if (args.size() != 1) {
            return null;
        }
        var recipes = new ArrayList<>(plugin.getStones().getIds());
        recipes.add("REFORGING_ANVIL");
        return recipes;
    }
}
