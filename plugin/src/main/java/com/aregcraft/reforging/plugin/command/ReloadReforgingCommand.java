package com.aregcraft.reforging.plugin.command;

import com.aregcraft.reforging.core.SimpleCommand;
import com.aregcraft.reforging.plugin.Reforging;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadReforgingCommand extends SimpleCommand<CommandSender> {
    public ReloadReforgingCommand() {
        super("reloadreforging", 0, CommandSender.class);
    }

    @Override
    public boolean perform(CommandSender sender, List<String> arguments) {
        Bukkit.recipeIterator().forEachRemaining(it -> {
            if (!(it instanceof Keyed keyed)) {
                return;
            }
            var key = keyed.getKey();
            if (Reforging.plugin().getName().toLowerCase().equals(key.getNamespace())) {
                Bukkit.removeRecipe(key);
            }
        });
        Reforging.loadConfig();
        return true;
    }
}
