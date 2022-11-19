package com.aregcraft.reforging.command;

import com.aregcraft.reforging.Reforging;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadReforgingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Reforging.CONFIG.load();
        return true;
    }
}
