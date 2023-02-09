package com.aregcraft.reforging.command;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.reforging.Reforging;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RegisteredCommand("updatereforging")
public class UpdateReforgingCommand implements CommandWrapper {
    @InjectPlugin
    private Reforging plugin;

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (args.size() != 0) {
            return false;
        }
        CompletableFuture.runAsync(plugin.getUpdater()::tryDownloadLatestVersion);
        return true;
    }
}
