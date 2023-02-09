package com.aregcraft.reforging.target;

import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;

public enum Helmet implements TargetItem {
    GOLDEN_HELMET(Material.GOLD_INGOT, 2, 0),
    IRON_HELMET(Material.IRON_INGOT, 2, 0),
    DIAMOND_HELMET(Material.DIAMOND, 3, 2),
    NETHERITE_HELMET(Material.NETHERITE_SCRAP, 3, 3);

    private final Material ingredient;
    private final double armor;
    private final double armorToughness;

    Helmet(Material ingredient, double armor, double armorToughness) {
        this.ingredient = ingredient;
        this.armor = armor;
        this.armorToughness = armorToughness;
    }

    public static Helmet of(ItemWrapper item) {
        return valueOf(item.getMaterial().name());
    }

    @Override
    public boolean isIngredient(ItemWrapper item) {
        return item.getMaterial() == ingredient;
    }

    @Override
    public void addAttributeModifiers(ItemWrapper item) {
        item.createAttributeModifierBuilder()
                .name("BASE_ARMOR")
                .attribute(Attribute.GENERIC_ARMOR)
                .amount(armor)
                .slot(EquipmentSlot.HEAD)
                .add();
        item.createAttributeModifierBuilder()
                .name("BASE_ARMOR_TOUGHNESS")
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS)
                .amount(armorToughness)
                .slot(EquipmentSlot.HEAD)
                .add();
    }

    @Override
    public EquipmentSlot getSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public double getArmor() {
        return armor;
    }

    @Override
    public double getArmorToughness() {
        return armorToughness;
    }
}
