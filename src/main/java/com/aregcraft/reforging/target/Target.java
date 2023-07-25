package com.aregcraft.reforging.target;

import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Arrays;
import java.util.function.Function;

public enum Target {
    SWORD(Sword::of),
    AXE(Axe::of),
    HELMET(Helmet::of),
    CHESTPLATE(Chestplate::of),
    LEGGINGS(Leggings::of),
    BOOTS(Boots::of);

    private final Function<ItemWrapper, ? extends TargetItem> provider;

    Target(Function<ItemWrapper, ? extends TargetItem> provider) {
        this.provider = provider;
    }

    public static boolean isTarget(String name) {
        return Arrays.stream(values()).anyMatch(it -> it.matches(name));
    }

    public static boolean isTarget(ItemWrapper item) {
        return Arrays.stream(values()).anyMatch(it -> it.matches(item));
    }

    public static boolean isWeapon(ItemWrapper item) {
        return SWORD.matches(item) || AXE.matches(item);
    }

    public static boolean isArmor(ItemWrapper item) {
        return HELMET.matches(item)
                || CHESTPLATE.matches(item)
                || LEGGINGS.matches(item)
                || BOOTS.matches(item);
    }

    public static boolean isIngredient(ItemWrapper item, ItemWrapper target) {
        return getTargetItem(target).isIngredient(item);
    }

    public static void addAttributeModifiers(ItemWrapper item) {
        getTargetItem(item).addAttributeModifiers(item);
    }

    public static EquipmentSlot getSlot(ItemWrapper item) {
        return getTargetItem(item).getSlot();
    }

    public static double getAttackDamage(ItemWrapper item) {
        return getTargetItem(item).getAttackDamage();
    }

    public static double getAttackSpeed(ItemWrapper item) {
        return getTargetItem(item).getAttackSpeed();
    }

    public static double getArmor(ItemWrapper item) {
        return getTargetItem(item).getArmor();
    }

    public static double getArmorToughness(ItemWrapper item) {
        return getTargetItem(item).getArmorToughness();
    }

    private static TargetItem getTargetItem(ItemWrapper item) {
        return Arrays.stream(values()).filter(it -> it.matches(item)).findAny().orElseThrow().provider.apply(item);
    }

    public boolean matches(String name) {
        return name.endsWith(name().toLowerCase());
    }

    public boolean matches(ItemWrapper item) {
        var name = item.getMaterial().name();
        return name.endsWith("_" + name()) && !name.startsWith("WOODEN")
                && !name.startsWith("LEATHER") && !name.startsWith("CHAINMAIL");
    }
}
