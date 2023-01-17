package com.aregcraft.reforging;

import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Arrays;

public enum Weapon {
    STONE_SWORD(Material.COBBLESTONE, 4, 1.6),
    GOLDEN_SWORD(Material.GOLD_INGOT, 5, 1.6),
    IRON_SWORD(Material.IRON_INGOT, 6, 1.6),
    DIAMOND_SWORD(Material.DIAMOND, 7, 1.6),
    NETHERITE_SWORD(Material.NETHERITE_SCRAP, 8, 1.6),
    STONE_AXE(Material.COBBLESTONE, 7, 1),
    GOLDEN_AXE(Material.GOLDEN_AXE, 9, 0.8),
    IRON_AXE(Material.IRON_AXE, 9, 0.9),
    DIAMOND_AXE(Material.DIAMOND, 9, 1),
    NETHERITE_AXE(Material.NETHERITE_SCRAP, 10, 1);

    public static final double BASE_ATTACK_SPEED = 4;

    private final Material ingredient;
    private final double attackDamage;
    private final double attackSpeed;

    Weapon(Material ingredient, double attackDamage, double attackSpeed) {
        this.ingredient = ingredient;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }

    public static Weapon of(ItemWrapper item) {
        return valueOf(item.getMaterial().name());
    }

    public static boolean isWeapon(ItemWrapper item) {
        return Arrays.stream(values()).map(Enum::name).anyMatch(item.getMaterial().name()::equals);
    }

    public static boolean isWeapon(String name) {
        return Arrays.stream(values())
                .map(Enum::name)
                .map(LanguageLoader::getLanguageKey)
                .anyMatch(name::equals);
    }

    public static boolean isIngredient(ItemWrapper item, ItemWrapper weapon) {
        return valueOf(weapon.getMaterial().name()).ingredient.equals(item.getMaterial());
    }

    public static boolean isReforgeStone(ItemWrapper item, Reforging plugin) {
        return item.getPersistentData(plugin).check("id", "reforge_stone");
    }

    public void addAttributeModifiers(ItemWrapper item) {
        item.createAttributeModifierBuilder()
                .name("BASE_ATTACK_DAMAGE")
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE)
                .amount(attackDamage)
                .slot(EquipmentSlot.HAND)
                .add();
        item.createAttributeModifierBuilder()
                .name("BASE_ATTACK_SPEED")
                .attribute(Attribute.GENERIC_ATTACK_SPEED)
                .amount(attackSpeed - BASE_ATTACK_SPEED)
                .slot(EquipmentSlot.HAND)
                .add();
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }
}
