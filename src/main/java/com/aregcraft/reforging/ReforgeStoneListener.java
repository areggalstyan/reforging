package com.aregcraft.reforging;

import com.aregcraft.delta.api.RegisteredListener;
import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

@RegisteredListener
public class ReforgeStoneListener implements Listener {
    private Reforging plugin;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(ItemWrapper.wrap(event.getItemInHand()).getPersistentData(plugin)
                .check("id", "reforge_stone"));
    }
}
