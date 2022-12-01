package com.aregcraft.reforging.command;

import com.aregcraft.reforging.Reforging;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadReforgingCommand extends SimpleCommand<CommandSender> {
    public ReloadReforgingCommand() {
        super("reloadreforging", 0, CommandSender.class);
    }

    @Override
    public boolean perform(CommandSender sender, List<String> arguments) {
        Reforging.loadConfig();
        return true;
    }
}
