package com.aregcraft.reforging.command;

import com.aregcraft.reforging.Reforge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class ReforgeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player) || args.length != 1 || !Reforge.REFORGES.containsKey(args[0])) {
            return false;
        }
        Reforge.REFORGES.get(args[0]).apply(player.getInventory().getItemInMainHand());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            return null;
        }
        return Reforge.REFORGES.keySet().stream().toList();
    }
}
