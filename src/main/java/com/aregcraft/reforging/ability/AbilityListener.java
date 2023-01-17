package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.RegisteredListener;
import com.aregcraft.delta.api.block.custom.CustomBlock;
import com.aregcraft.delta.api.entity.EquipmentWrapper;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.reforging.Reforging;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

@RegisteredListener
public class AbilityListener implements Listener {
    private Reforging plugin;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.HAND && isRightClick(event.getAction())
                && !CustomBlock.check(event.getClickedBlock(), "reforging_anvil", plugin)) {
            activateAbility(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            activateAbility(event.getPlayer());
        }
    }

    private boolean isRightClick(Action action) {
        return action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
    }

    private void activateAbility(Player player) {
        var item = EquipmentWrapper.wrap(player).getItemInMainHand();
        if (item.getMaterial().isAir()) {
            return;
        }
        var reforge = plugin.getReforge(item.getPersistentData(plugin).get("reforge", String.class));
        if (reforge != null) {
            reforge.activateAbility(player);
        }
    }
}
