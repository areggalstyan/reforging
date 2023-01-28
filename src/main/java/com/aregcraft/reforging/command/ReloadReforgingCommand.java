package com.aregcraft.reforging.command;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.reforging.Reforging;
import org.bukkit.command.CommandSender;

import java.util.List;

@RegisteredCommand("reloadreforging")
public class ReloadReforgingCommand implements CommandWrapper {
    @InjectPlugin
    private Reforging plugin;

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        plugin.reload();
        return true;
    }
}
