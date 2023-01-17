package com.aregcraft.reforging;

import com.aregcraft.delta.api.block.BlockWrapper;
import com.aregcraft.delta.api.block.custom.CustomBlock;
import com.aregcraft.delta.api.block.custom.RegisteredCustomBlock;
import com.aregcraft.delta.api.entity.EntityBuilder;
import com.aregcraft.delta.api.entity.EquipmentWrapper;
import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.EulerAngle;

import java.util.Optional;

@RegisteredCustomBlock("reforging_anvil")
public class ReforgingAnvilBlock implements CustomBlock {
    private Reforging plugin;
    private ArmorStand armorStand;

    @Override
    public ItemWrapper getItem() {
        return plugin.getReforgingAnvil().getItem();
    }

    @Override
    public void onEnable(Block block) {
        if (hasWeapon(block)) {
            setWeapon(block, getWeapon(block));
        }
    }

    @Override
    public void onDisable(Block block) {
        Optional.ofNullable(armorStand).ifPresent(Entity::remove);
    }

    @Override
    public void onBreak(BlockBreakEvent event) {
        dropWeapon(event.getBlock());
    }

    @Override
    public void onRightClick(PlayerInteractEvent event) {
        event.setCancelled(true);
        var item = ItemWrapper.wrap(event.getItem());
        var block = event.getClickedBlock();
        if (item == null) {
            dropWeapon(block);
            return;
        }
        if (Weapon.isWeapon(item)) {
            dropWeapon(block);
            setWeapon(block, item);
            item.decrementAmount();
            return;
        }
        if (!hasWeapon(block)) {
            return;
        }
        var player = event.getPlayer();
        if (Weapon.isReforgeStone(item, plugin)) {
            applyReforge(block, player, plugin.getUltimateReforge(item));
            item.decrementAmount();
            return;
        }
        if (!Weapon.isIngredient(item, getWeapon(block))) {
            return;
        }
        if (plugin.getReforgingAnvil().deductPrice(item)) {
            applyReforge(block, player, plugin.getRandomStandardReforge());
        }
    }

    private void applyReforge(Block block, Player player, Reforge reforge) {
        setWeapon(block, reforge.apply(player, getWeapon(block), plugin));
        dropWeapon(block);
        plugin.getReforgingAnvil().playSound(block);
    }

    private void dropWeapon(Block block) {
        if (hasWeapon(block)) {
            getWeapon(block).dropNaturally(block.getLocation().add(0, 1, 0));
            setWeapon(block, null);
        }
        Optional.ofNullable(armorStand).ifPresent(Entity::remove);
        armorStand = null;
    }

    private boolean hasWeapon(Block block) {
        return BlockWrapper.wrap(block, plugin).getPersistentData().has("weapon", ItemWrapper.class);
    }

    private ItemWrapper getWeapon(Block block) {
        return BlockWrapper.wrap(block, plugin).getPersistentData().get("weapon", ItemWrapper.class);
    }

    private void setWeapon(Block block, ItemWrapper weapon) {
        BlockWrapper.wrap(block, plugin).getPersistentData().set("weapon", weapon);
        if (armorStand == null) {
            createArmorStand(block);
        }
        EquipmentWrapper.wrap(armorStand).setItemInMainHand(weapon);
    }

    private void createArmorStand(Block block) {
        var location = block.getLocation().subtract(0, 0.25, 0);
        switch (((Directional) block.getBlockData()).getFacing()) {
            case NORTH, SOUTH -> location.add(0.25, 0, 0);
            case EAST, WEST -> {
                location.add(0, 0, 0.75);
                location.setYaw(270);
            }
        }
        armorStand = EntityBuilder.createArmorStand().build(location, plugin);
        armorStand.setRightArmPose(new EulerAngle(0, 0, 1.5 * Math.PI));
    }
}
