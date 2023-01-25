package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.PersistentDataWrapper;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Allows the player briefly spectate while retaining their location
 */
public class SpectateAbility extends Ability implements Listener {
    /**
     * The duration in ticks (1 second = 20 ticks)
     */
    private long duration;

    @Override
    protected void perform(Player player) {
        var mode = player.getGameMode();
        var location = player.getLocation();
        player.setGameMode(GameMode.SPECTATOR);
        PersistentDataWrapper.wrap(getPlugin(), player).set("mode", mode);
        setPlayerActive(player, duration, () -> resetPlayer(player, mode, location));
    }

    private void resetPlayer(Player player, GameMode mode, Location location) {
        if (!isPlayerActive(player)) {
            return;
        }
        player.setGameMode(mode);
        player.teleport(location);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getEntity();
        player.setGameMode(PersistentDataWrapper.wrap(getPlugin(), player).get("mode", GameMode.class));
        setPlayerInactive(player);
    }
}
