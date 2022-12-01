package com.aregcraft.reforging.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public abstract class SimpleCommand<T extends CommandSender> implements TabExecutor {
    private final int argumentCount;
    private final Class<T> senderType;

    protected SimpleCommand(String name, int argumentCount, Class<T> senderType) {
        this.argumentCount = argumentCount;
        this.senderType = senderType;
        var command = Bukkit.getPluginCommand(name);
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (senderType.isInstance(sender) && args.length == argumentCount) {
            return perform(senderType.cast(sender), List.of(args));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (senderType.isInstance(sender)) {
            return suggest(senderType.cast(sender), List.of(args));
        }
        return null;
    }

    public List<String> suggest(T sender, List<String> arguments) {
        return null;
    }

    public abstract boolean perform(T sender, List<String> arguments);
}
