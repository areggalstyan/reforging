package com.aregcraft.reforging.target;

import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;

public enum Sword implements TargetItem {
    STONE_SWORD(Material.COBBLESTONE, 4, 1.6),
    GOLDEN_SWORD(Material.GOLD_INGOT, 5, 1.6),
    IRON_SWORD(Material.IRON_INGOT, 6, 1.6),
    DIAMOND_SWORD(Material.DIAMOND, 7, 1.6),
    NETHERITE_SWORD(Material.NETHERITE_SCRAP, 8, 1.6);

    public static final double BASE_ATTACK_SPEED = 4;

    private final Material ingredient;
    private final double attackDamage;
    private final double attackSpeed;

    Sword(Material ingredient, double attackDamage, double attackSpeed) {
        this.ingredient = ingredient;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }

    public static Sword of(ItemWrapper item) {
        return valueOf(item.getMaterial().name());
    }

    @Override
    public boolean isIngredient(ItemWrapper item) {
        return item.getMaterial() == ingredient;
    }

    @Override
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

    @Override
    public EquipmentSlot getSlot() {
        return EquipmentSlot.HAND;
    }

    @Override
    public double getAttackDamage() {
        return attackDamage;
    }

    @Override
    public double getAttackSpeed() {
        return attackSpeed;
    }
}
