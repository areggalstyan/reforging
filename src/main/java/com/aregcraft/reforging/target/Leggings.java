package com.aregcraft.reforging.target;

import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;

public enum Leggings implements TargetItem {
    GOLDEN_LEGGINGS(Material.GOLD_INGOT, 3, 0),
    IRON_LEGGINGS(Material.IRON_INGOT, 5, 0),
    DIAMOND_LEGGINGS(Material.DIAMOND, 6, 2),
    NETHERITE_LEGGINGS(Material.NETHERITE_SCRAP, 6, 3);

    private final Material ingredient;
    private final double armor;
    private final double armorToughness;

    Leggings(Material ingredient, double armor, double armorToughness) {
        this.ingredient = ingredient;
        this.armor = armor;
        this.armorToughness = armorToughness;
    }

    public static Leggings of(ItemWrapper item) {
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
                .slot(EquipmentSlot.LEGS)
                .add();
        item.createAttributeModifierBuilder()
                .name("BASE_ARMOR_TOUGHNESS")
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS)
                .amount(armorToughness)
                .slot(EquipmentSlot.LEGS)
                .add();
    }

    @Override
    public EquipmentSlot getSlot() {
        return EquipmentSlot.LEGS;
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
