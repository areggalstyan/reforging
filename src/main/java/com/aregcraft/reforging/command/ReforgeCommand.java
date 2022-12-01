package com.aregcraft.reforging.command;

import com.aregcraft.reforging.Reforging;
import org.bukkit.entity.Player;

import java.util.List;

public class ReforgeCommand extends SimpleCommand<Player> {
    public ReforgeCommand() {
        super("reforge", 1, Player.class);
    }

    @Override
    public boolean perform(Player sender, List<String> arguments) {
        var name = arguments.get(0);
        if (Reforging.config().reforges().containsKey(name)) {
            return Reforging.config().reforges().get(name).apply(sender.getInventory().getItemInMainHand());
        }
        return false;
    }

    @Override
    public List<String> suggest(Player sender, List<String> arguments) {
        return arguments.size() == 1 ? Reforging.config().reforges().keySet().stream().toList() : null;
    }
}
