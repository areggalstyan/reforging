package com.aregcraft.reforging;

import com.aregcraft.reforging.format.ChatText;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class ReforgingAnvil implements Listener {
    public static final ItemStack ITEM = new ItemStack(Material.ANVIL);
    public static final NamespacedKey KEY = new NamespacedKey(Reforging.PLUGIN, "id");
    public static final Set<Material> TYPES = Set.of(
            Material.WOODEN_SWORD,
            Material.STONE_SWORD,
            Material.IRON_SWORD,
            Material.GOLDEN_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD,
            Material.WOODEN_AXE,
            Material.STONE_AXE,
            Material.IRON_AXE,
            Material.GOLDEN_AXE,
            Material.DIAMOND_AXE,
            Material.NETHERITE_AXE
    );
    public static final Multimap<Material, Material> MATERIALS = ImmutableMultimap.<Material, Material>builder()
            .put(Material.WOODEN_SWORD, Material.OAK_PLANKS)
            .put(Material.WOODEN_SWORD, Material.ACACIA_PLANKS)
            .put(Material.WOODEN_SWORD, Material.BIRCH_PLANKS)
            .put(Material.WOODEN_SWORD, Material.CRIMSON_PLANKS)
            .put(Material.WOODEN_SWORD, Material.DARK_OAK_PLANKS)
            .put(Material.WOODEN_SWORD, Material.JUNGLE_PLANKS)
            .put(Material.WOODEN_SWORD, Material.SPRUCE_PLANKS)
            .put(Material.WOODEN_SWORD, Material.WARPED_PLANKS)
            .put(Material.STONE_SWORD, Material.COBBLESTONE)
            .put(Material.IRON_SWORD, Material.IRON_INGOT)
            .put(Material.GOLDEN_SWORD, Material.GOLD_INGOT)
            .put(Material.DIAMOND_SWORD, Material.DIAMOND)
            .put(Material.NETHERITE_SWORD, Material.NETHERITE_SCRAP)
            .put(Material.STONE_AXE, Material.COBBLESTONE)
            .put(Material.IRON_AXE, Material.IRON_INGOT)
            .put(Material.GOLDEN_AXE, Material.GOLD_INGOT)
            .put(Material.DIAMOND_AXE, Material.DIAMOND)
            .put(Material.NETHERITE_AXE, Material.NETHERITE_SCRAP)
            .build();

    public ReforgingAnvil() {
        var itemMeta = ITEM.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        itemMeta.setDisplayName(ChatText.colorize(Reforging.CONFIG.anvil.name));
        itemMeta.setLore(Reforging.CONFIG.anvil.lore.stream().map(ChatText::colorize).toList());
        itemMeta.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, "REFORGING_ANVIL");
        ITEM.setItemMeta(itemMeta);
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(Reforging.PLUGIN, "reforging_anvil"), ITEM);
        recipe.shape(Reforging.CONFIG.anvil.recipe.shape);
        Reforging.CONFIG.anvil.recipe.keys.forEach((key, value) -> recipe.setIngredient(key.charAt(0), value));
        Bukkit.addRecipe(recipe);
        Bukkit.getPluginManager().registerEvents(this, Reforging.PLUGIN);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var itemMeta = event.getItemInHand().getItemMeta();
        if (itemMeta == null || !isReforgingAnvil(itemMeta)) {
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
        var armorStand = (ArmorStand) block.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setCanPickupItems(false);
        armorStand.setArms(true);
        armorStand.setRightArmPose(new EulerAngle(0, 0, 1.5 * Math.PI));
        armorStand.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, "REFORGING_ANVIL");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var block = event.getBlock();
        if (!isReforgingAnvil(block)) {
            return;
        }
        var armorStand = getArmorStandAt(block);
        var item = Objects.requireNonNull(armorStand.getEquipment()).getItemInMainHand();
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
        var equipment = Objects.requireNonNull(getArmorStandAt(block).getEquipment());
        var previousItem = equipment.getItemInMainHand();
        var item = event.getItem();
        if (item == null) {
            dropItem(block, previousItem, equipment);
            return;
        }
        var type = item.getType();
        if (!TYPES.contains(type) && !MATERIALS.containsEntry(previousItem.getType(), type)) {
            dropItem(block, previousItem, equipment);
            return;
        }
        var player = event.getPlayer();
        var inventory = player.getInventory();
        if (TYPES.contains(type)) {
            dropItem(block, previousItem, equipment);
            equipment.setItemInMainHand(item);
            inventory.setItemInMainHand(null);
            return;
        }
        var amount = item.getAmount();
        if (amount < Reforging.CONFIG.anvil.price) {
            return;
        }
        item.setAmount(amount - Reforging.CONFIG.anvil.price);
        Reforging.CONFIG.reforgeSampler.sample().apply(previousItem);
        dropItem(block, previousItem, equipment);
        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_ANVIL_USE, Reforging.CONFIG.anvil.soundEffect.volume,
                Reforging.CONFIG.anvil.soundEffect.pitch);
    }

    private void dropItem(Block block, ItemStack item, EntityEquipment equipment) {
        if (item.getType() != Material.AIR) {
            block.getWorld().dropItemNaturally(block.getLocation().add(0, 1, 0), item);
        }
        equipment.setItemInMainHand(null);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.setCancelled(isReforgingAnvil(event.getBlock()));
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        event.setCancelled(event.getBlocks().stream().anyMatch(this::isReforgingAnvil));
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        event.setCancelled(event.getBlocks().stream().anyMatch(this::isReforgingAnvil));
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (isReforgingAnvil(event.getRightClicked())) {
            event.setCancelled(true);
        }
    }

    private boolean isReforgingAnvil(Block block) {
        return block.getType() == Material.ANVIL && getEntitiesAt(block).anyMatch(__ -> true);
    }

    private Stream<Entity> getEntitiesAt(Block block) {
        return block.getWorld().getNearbyEntities(BoundingBox.of(block)).stream().filter(this::isReforgingAnvil);
    }

    private ArmorStand getArmorStandAt(Block block) {
        return (ArmorStand) getEntitiesAt(block).findAny().orElseThrow();
    }

    private boolean isReforgingAnvil(PersistentDataHolder dataHolder) {
        return "REFORGING_ANVIL".equals(dataHolder.getPersistentDataContainer().get(KEY, PersistentDataType.STRING));
    }
}
