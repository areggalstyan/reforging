package com.aregcraft.reforging.target;

import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;

public enum Axe implements TargetItem {
    STONE_AXE(Material.COBBLESTONE, 7, 1),
    GOLDEN_AXE(Material.GOLD_INGOT, 9, 0.8),
    IRON_AXE(Material.IRON_INGOT, 9, 0.9),
    DIAMOND_AXE(Material.DIAMOND, 9, 1),
    NETHERITE_AXE(Material.NETHERITE_SCRAP, 10, 1);

    public static final double BASE_ATTACK_SPEED = 4;

    private final Material ingredient;
    private final double attackDamage;
    private final double attackSpeed;

    Axe(Material ingredient, double attackDamage, double attackSpeed) {
        this.ingredient = ingredient;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }

    public static Axe of(ItemWrapper item) {
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
