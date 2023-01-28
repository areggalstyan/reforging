package com.aregcraft.reforging;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.RegisteredListener;
import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@RegisteredListener
public class ReforgeStoneListener implements Listener {
    @InjectPlugin
    private Reforging plugin;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(isReforgeStone(event.getItemInHand()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        event.setCancelled(isReforgeStone(event.getItem()));
    }

    private boolean isReforgeStone(ItemStack item) {
        return item != null && ItemWrapper.wrap(item).getPersistentData(plugin).check("id", "reforge_stone");
    }
}
