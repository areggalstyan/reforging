package com.aregcraft.reforging.plugin;

import com.aregcraft.reforging.plugin.item.ItemStackWrapper;
import com.aregcraft.reforging.plugin.item.Weapon;
import com.aregcraft.reforging.plugin.util.Spawner;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.EulerAngle;

import java.util.Optional;

public class ReforgingAnvil implements Listener {
    private static final ItemStack ITEM = ItemStackWrapper.fromModel(Material.ANVIL, Reforging.config().anvil().item(),
            "REFORGING_ANVIL").unwrap();

    public ReforgingAnvil() {
        var recipe = new ShapedRecipe(Reforging.key("reforging_anvil"), ITEM);
        recipe.shape(Reforging.config().anvil().recipe().shape());
        Reforging.config().anvil().recipe().keys().forEach(recipe::setIngredient);
        Bukkit.addRecipe(recipe);
        Bukkit.getPluginManager().registerEvents(this, Reforging.plugin());
    }

    private static boolean isReforgingAnvil(ItemStackWrapper item) {
        return "REFORGING_ANVIL".equals(item.get("id"));
    }

    private static boolean isReforgingAnvil(Block block) {
        return block.getType() == Material.ANVIL && armorStandAt(block) != null;
    }

    private static boolean isReforgingAnvil(Entity entity) {
        return "REFORGING_ANVIL".equals(entity.getPersistentDataContainer().get(Reforging.key("id"),
                PersistentDataType.STRING));
    }

    private static ArmorStand armorStandAt(Block block) {
        return (ArmorStand) Spawner.nearbyEntities(block).stream().filter(ReforgingAnvil::isReforgingAnvil).findAny()
                .orElse(null);
    }

    private static void dropItemAndPlaySound(EntityEquipment equipment, ItemStackWrapper weapon, World world,
                                             Location location) {
        world.dropItemNaturally(location, weapon.unwrap());
        equipment.setItemInMainHand(null);
        world.playSound(location, Sound.BLOCK_ANVIL_USE, Reforging.config().anvil().sound().volume(),
                Reforging.config().anvil().sound().pitch());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var item = ItemStackWrapper.wrap(event.getItemInHand());
        if (item == null || !isReforgingAnvil(item)) {
            return;
        }
        var block = event.getBlock();
        var location = block.getLocation().subtract(0, 0.25, 0);
        var facing = ((Directional) block.getBlockData()).getFacing();
        if (facing == BlockFace.NORTH || facing == BlockFace.SOUTH) {
            location.add(0.25, 0, 0);
        } else {
            location.add(0, 0, 0.75);
            location.setYaw(270);
        }
        var armorStand = Spawner.spawnArmorStand(location);
        armorStand.setRightArmPose(new EulerAngle(0, 0, 1.5 * Math.PI));
        armorStand.getPersistentDataContainer().set(Reforging.key("id"), PersistentDataType.STRING,
                "REFORGING_ANVIL");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var block = event.getBlock();
        if (!isReforgingAnvil(block)) {
            return;
        }
        var armorStand = armorStandAt(block);
        var item = armorStand.getEquipment().getItemInMainHand();
        armorStand.remove();
        if (!event.isDropItems()) {
            return;
        }
        event.setDropItems(false);
        var world = block.getWorld();
        var location = block.getLocation();
        world.dropItemNaturally(location, ITEM);
        if (item.getType() != Material.AIR) {
            world.dropItemNaturally(location, item);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        var block = event.getClickedBlock();
        if (block == null || !isReforgingAnvil(block)) {
            return;
        }
        event.setCancelled(true);
        var equipment = armorStandAt(block).getEquipment();
        var weapon = ItemStackWrapper.wrap(equipment.getItemInMainHand());
        var item = ItemStackWrapper.wrap(event.getItem());
        var world = block.getWorld();
        var location = block.getLocation().add(0, 1, 0);
        if (item == null || Weapon.has(item)) {
            if (weapon != null && weapon.material() != Material.AIR) {
                world.dropItemNaturally(location, weapon.unwrap());
            }
            equipment.setItemInMainHand(Optional.ofNullable(item).map(ItemStackWrapper::unwrap).orElse(null));
            event.getPlayer().getInventory().setItemInMainHand(null);
            return;
        }
        if (weapon == null) {
            return;
        }
        var id = item.get("id");
        if (id != null && Reforging.config().reforgeStones().containsKey(id)) {
            Reforging.config().reforges().get(Reforging.config().reforgeStones().get(id)).apply(weapon);
            item.amount(item.amount() - 1);
            dropItemAndPlaySound(equipment, weapon, world, location);
            return;
        }
        if (Weapon.of(weapon).material().equals(item.material())) {
            Reforging.config().reforgeSampler().sample().apply(weapon);
            item.amount(item.amount() - Reforging.config().anvil().price());
            dropItemAndPlaySound(equipment, weapon, world, location);
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.setCancelled(isReforgingAnvil(event.getBlock()));
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        event.setCancelled(event.getBlocks().stream().anyMatch(ReforgingAnvil::isReforgingAnvil));
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        event.setCancelled(event.getBlocks().stream().anyMatch(ReforgingAnvil::isReforgingAnvil));
    }
}
