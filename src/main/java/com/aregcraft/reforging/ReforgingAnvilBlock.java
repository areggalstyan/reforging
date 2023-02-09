package com.aregcraft.reforging;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.block.BlockWrapper;
import com.aregcraft.delta.api.block.custom.CustomBlock;
import com.aregcraft.delta.api.block.custom.RegisteredCustomBlock;
import com.aregcraft.delta.api.entity.EntityBuilder;
import com.aregcraft.delta.api.entity.EquipmentWrapper;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.reforging.target.Target;
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
    public static final double HELMET_OFFSET = -0.5;
    public static final double CHESTPLATE_OFFSET = 0.25;
    public static final double OFFSET_LEGGINGS = 0.75;
    public static final int BOOTS_OFFSET = 1;
    public static final double DEFAULT_OFFSET = -0.25;

    @InjectPlugin
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
        if (Target.isTarget(item)) {
            dropWeapon(block);
            setWeapon(block, item);
            item.decrementAmount();
            return;
        }
        if (!hasWeapon(block)) {
            return;
        }
        var player = event.getPlayer();
        var weapon = getWeapon(block);
        var ultimateReforge = plugin.getUltimateReforge(item);
        if (plugin.isStone(item) && ultimateReforge.isApplicable(weapon)) {
            applyReforge(block, player, ultimateReforge);
            item.decrementAmount();
            return;
        }
        if (!Target.isIngredient(item, weapon)) {
            return;
        }
        var standardReforge = plugin.getRandomStandardReforge(weapon);
        if (standardReforge != null && plugin.getReforgingAnvil().deductPrice(item)) {
            applyReforge(block, player, standardReforge);
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
        if (weapon == null) {
            return;
        }
        if (armorStand == null) {
            createArmorStand(block);
        }
        EquipmentWrapper.wrap(armorStand).setItem(Target.getSlot(weapon), weapon);
    }

    private void createArmorStand(Block block) {
        var weapon = getWeapon(block);
        var location = block.getLocation().add(0, switch (Target.getSlot(weapon)) {
            case HEAD -> HELMET_OFFSET;
            case CHEST -> CHESTPLATE_OFFSET;
            case LEGS -> OFFSET_LEGGINGS;
            case FEET -> BOOTS_OFFSET;
            default -> DEFAULT_OFFSET;
        }, 0);
        if (Target.isArmor(weapon)) {
            armorStand = EntityBuilder.createArmorStand().build(location.add(0.5, 0, 0.5), plugin);
            return;
        }
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
