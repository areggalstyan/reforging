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
        if (isReforgeStone(event.getItemInHand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (isReforgeStone(event.getItem())) {
            event.setCancelled(true);
        }
    }

    private boolean isReforgeStone(ItemStack item) {
        if (item == null || item.getItemMeta() == null) {
            return false;
        }
        return ItemWrapper.wrap(item).getPersistentData(plugin).check("id", "reforge_stone");
    }
}
