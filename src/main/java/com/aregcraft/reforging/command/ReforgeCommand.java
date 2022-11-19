package com.aregcraft.reforging.command;

import com.aregcraft.reforging.Reforging;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class ReforgeCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player) || args.length != 1
                || !Reforging.CONFIG.reforges.containsKey(args[0])) {
            return false;
        }
        Reforging.CONFIG.reforges.get(args[0]).apply(player.getInventory().getItemInMainHand());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            return null;
        }
        return Reforging.CONFIG.reforges.keySet().stream().toList();
    }
}
