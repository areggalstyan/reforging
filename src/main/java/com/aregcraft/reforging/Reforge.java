package com.aregcraft.reforging;

import com.aregcraft.reforging.data.Abilities;
import com.aregcraft.reforging.ability.Ability;
import com.aregcraft.reforging.data.Item;
import com.aregcraft.reforging.util.ChatText;
import com.aregcraft.reforging.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public record Reforge(String name, Ability ability, float maxHealth, float knockbackResistance,
                      float movementSpeed, float attackDamage, float armor, float armorToughness, float attackSpeed,
                      float attackKnockback, int weight) implements Listener {

    public static final Map<String, Ability> ABILITIES = new HashMap<>();
    static {
        var abilities = Config.readFile("abilities", Abilities.class);
        ABILITIES.put("DUMMY", Ability.DUMMY);
        ABILITIES.putAll(abilities.shieldAbilities);
        ABILITIES.putAll(abilities.fireAbilities);
        ABILITIES.putAll(abilities.throwAbilities);
        ABILITIES.putAll(abilities.stormAbilities);
        ABILITIES.putAll(abilities.earthAbilities);
    }
    public static final Item ITEM = Config.readFile("item", Item.class);
    public static final Map<String, Reforge> REFORGES = List.of(Config.readFile("reforges", Reforge[].class))
            .stream().collect(Collectors.toMap(Reforge::name, Function.identity()));
    public static final NamespacedKey REFORGE_KEY = new NamespacedKey(Reforging.PLUGIN, "reforge");
    public static final TreeMap<Integer, Reforge> WEIGHTS = new TreeMap<>();
    public static final int TOTAL_WEIGHT;
    static {
        int totalWeight = 0;
        for (var reforge : REFORGES.values()) {
            totalWeight += reforge.weight;
            WEIGHTS.put(totalWeight, reforge);
        }
        TOTAL_WEIGHT = totalWeight;
    }
    public static final NamespacedKey DISPLAY_NAME_KEY = new NamespacedKey(Reforging.PLUGIN, "display_name");
    public static final Map<Material, Integer> ATTACK_DAMAGES = Map.ofEntries(
            Map.entry(Material.WOODEN_SWORD, 4),
            Map.entry(Material.STONE_SWORD, 5),
            Map.entry(Material.IRON_SWORD, 6),
            Map.entry(Material.GOLDEN_SWORD, 4),
            Map.entry(Material.DIAMOND_SWORD, 7),
            Map.entry(Material.NETHERITE_SWORD, 8),
            Map.entry(Material.WOODEN_AXE, 7),
            Map.entry(Material.STONE_AXE, 9),
            Map.entry(Material.IRON_AXE, 9),
            Map.entry(Material.GOLDEN_AXE, 7),
            Map.entry(Material.DIAMOND_AXE, 9),
            Map.entry(Material.NETHERITE_AXE, 10)
    );
    public static final Map<Material, Float> ATTACK_SPEEDS = Map.ofEntries(
            Map.entry(Material.WOODEN_SWORD, 1.6f),
            Map.entry(Material.STONE_SWORD, 1.6f),
            Map.entry(Material.IRON_SWORD, 1.6f),
            Map.entry(Material.GOLDEN_SWORD, 1.6f),
            Map.entry(Material.DIAMOND_SWORD, 1.6f),
            Map.entry(Material.NETHERITE_SWORD, 1.6f),
            Map.entry(Material.WOODEN_AXE, 0.8f),
            Map.entry(Material.STONE_AXE, 0.8f),
            Map.entry(Material.IRON_AXE, 0.9f),
            Map.entry(Material.GOLDEN_AXE, 1f),
            Map.entry(Material.DIAMOND_AXE, 1f),
            Map.entry(Material.NETHERITE_AXE, 1f)
    );
    public static final Map<Material, String> NAMES = Map.ofEntries(
            Map.entry(Material.WOODEN_SWORD, "Wooden Sword"),
            Map.entry(Material.STONE_SWORD, "Stone Sword"),
            Map.entry(Material.IRON_SWORD, "Iron Sword"),
            Map.entry(Material.GOLDEN_SWORD, "Golden Sword"),
            Map.entry(Material.DIAMOND_SWORD, "Diamond Sword"),
            Map.entry(Material.NETHERITE_SWORD, "Netherite Sword"),
            Map.entry(Material.WOODEN_AXE, "Wooden Axe"),
            Map.entry(Material.STONE_AXE, "Stone Axe"),
            Map.entry(Material.IRON_AXE, "Iron Axe"),
            Map.entry(Material.GOLDEN_AXE, "Golden Axe"),
            Map.entry(Material.DIAMOND_AXE, "Diamond Axe"),
            Map.entry(Material.NETHERITE_AXE, "Netherite Axe")
    );

    public Reforge {
        Bukkit.getPluginManager().registerEvents(this, Reforging.PLUGIN);
    }

    public void apply(ItemStack item) {
        var itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        var dataContainer = itemMeta.getPersistentDataContainer();
        var type = item.getType();
        if (dataContainer.has(REFORGE_KEY, PersistentDataType.STRING)) {
            var previousName = dataContainer.get(REFORGE_KEY, PersistentDataType.STRING);
            removeAllAttributeModifiers(itemMeta, previousName);
            var lore = itemMeta.getLore();
            if (lore != null) {
                var previousReforge = REFORGES.get(previousName);
                ITEM.lore.stream().filter(Predicate.not(previousReforge::containsAbsent))
                        .map(it -> previousReforge.format(it, type)).mapToInt(lore::lastIndexOf)
                        .filter(it -> it > -1).forEach(lore::remove);
                itemMeta.setLore(lore);
            }
        }
        if (!dataContainer.has(DISPLAY_NAME_KEY, PersistentDataType.STRING)) {
            dataContainer.set(DISPLAY_NAME_KEY, PersistentDataType.STRING, itemMeta.hasDisplayName()
                    ? itemMeta.getDisplayName()
                    : NAMES.get(type));
        }
        dataContainer.set(REFORGE_KEY, PersistentDataType.STRING, name);
        addAllAttributeModifiers(itemMeta, type);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        var displayName = dataContainer.get(DISPLAY_NAME_KEY, PersistentDataType.STRING);
        if (displayName != null) {
            itemMeta.setDisplayName(ChatText.format(ITEM.name, Map.of("%REFORGE_NAME%", name,
                    "%NAME%", displayName)));
        }
        var lore = Objects.requireNonNullElse(itemMeta.getLore(), new ArrayList<String>());
        ITEM.lore.stream().filter(Predicate.not(this::containsAbsent))
                .map(it -> format(it, type)).forEach(lore::add);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }

    private boolean containsAbsent(String line) {
        return line.contains("%MAX_HEALTH%") && maxHealth == 0
                || line.contains("%KNOCKBACK_RESISTANCE%") && knockbackResistance == 0
                || line.contains("%MOVEMENT_SPEED%") && movementSpeed == 0
                || line.contains("%ARMOR%") && armor == 0
                || line.contains("%ARMOR_TOUGHNESS") && armorToughness == 0
                || line.contains("%ATTACK_KNOCKBACK%") && attackKnockback == 0;
    }

    private String format(String line, Material type) {
        return ChatText.format(line, Map.of(
                "%BASE_ATTACK_SPEED%", ATTACK_SPEEDS.get(type),
                "%ATTACK_SPEED%", attackSpeed,
                "%BASE_ATTACK_DAMAGE%", ATTACK_DAMAGES.get(type),
                "%ATTACK_DAMAGE%", attackDamage,
                "%MAX_HEALTH%", maxHealth,
                "%KNOCKBACK_RESISTANCE%", knockbackResistance,
                "%MOVEMENT_SPEED%", movementSpeed,
                "%ARMOR%", armor,
                "%ARMOR_TOUGHNESS%", armorToughness,
                "%ATTACK_KNOCKBACK%", attackKnockback
        ), new DecimalFormat()::format);
    }

    private void removeAttributeModifier(ItemMeta itemMeta, Attribute attribute, String previousName) {
        Optional.ofNullable(itemMeta.getAttributeModifiers(attribute)).orElse(Collections.emptyList())
                .stream().filter(it -> it.getName().equals(previousName))
                .forEach(it -> itemMeta.removeAttributeModifier(attribute, it));
    }

    private void removeAllAttributeModifiers(ItemMeta itemMeta, String previousName) {
        removeAttributeModifier(itemMeta, Attribute.GENERIC_MAX_HEALTH, previousName);
        removeAttributeModifier(itemMeta, Attribute.GENERIC_KNOCKBACK_RESISTANCE, previousName);
        removeAttributeModifier(itemMeta, Attribute.GENERIC_MOVEMENT_SPEED, previousName);
        removeAttributeModifier(itemMeta, Attribute.GENERIC_ATTACK_DAMAGE, previousName);
        removeAttributeModifier(itemMeta, Attribute.GENERIC_ARMOR, previousName);
        removeAttributeModifier(itemMeta, Attribute.GENERIC_ARMOR_TOUGHNESS, previousName);
        removeAttributeModifier(itemMeta, Attribute.GENERIC_ATTACK_SPEED, previousName);
        removeAttributeModifier(itemMeta, Attribute.GENERIC_ATTACK_KNOCKBACK, previousName);
    }

    private void addAllAttributeModifiers(ItemMeta itemMeta, Material type) {
        addAttributeModifier(itemMeta, Attribute.GENERIC_MAX_HEALTH, maxHealth);
        addAttributeModifier(itemMeta, Attribute.GENERIC_KNOCKBACK_RESISTANCE, knockbackResistance);
        addAttributeModifier(itemMeta, Attribute.GENERIC_MOVEMENT_SPEED, movementSpeed);
        addAttributeModifier(itemMeta, Attribute.GENERIC_ATTACK_DAMAGE,
                attackDamage + ATTACK_DAMAGES.get(type));
        addAttributeModifier(itemMeta, Attribute.GENERIC_ARMOR, armor);
        addAttributeModifier(itemMeta, Attribute.GENERIC_ARMOR_TOUGHNESS, armorToughness);
        addAttributeModifier(itemMeta, Attribute.GENERIC_ATTACK_SPEED,
                attackSpeed + ATTACK_SPEEDS.get(type) - 4);
        addAttributeModifier(itemMeta, Attribute.GENERIC_ATTACK_KNOCKBACK, attackKnockback);
    }

    private void addAttributeModifier(ItemMeta itemMeta, Attribute attribute, float value) {
        itemMeta.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), name, value,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            activateAbility(event.getItem(), event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        activateAbility(event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer());
    }

    private void activateAbility(ItemStack item, Player player) {
        if (item == null) {
            return;
        }
        var itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        var dataContainer = itemMeta.getPersistentDataContainer();
        if (!dataContainer.has(REFORGE_KEY, PersistentDataType.STRING)) {
            return;
        }
        var ability = REFORGES.get(dataContainer.get(REFORGE_KEY, PersistentDataType.STRING)).ability;
        player.damage(ability.price.health);
        player.setFoodLevel(Math.max(player.getFoodLevel() - ability.price.food, 0));
        ability.activate(player);
    }
}
