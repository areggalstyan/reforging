package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.FormattingContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Executes a command as the console with %player% placeholder upon usage
 */
public class CommandAbility extends Ability {
    /**
     * The command
     */
    private String command;

    @Override
    protected void perform(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getFormattingContext(player).format(command));
    }

    private FormattingContext getFormattingContext(Player player) {
        return FormattingContext.builder()
                .plugin(getPlugin())
                .placeholder("player", player.getName())
                .build();
    }
}
