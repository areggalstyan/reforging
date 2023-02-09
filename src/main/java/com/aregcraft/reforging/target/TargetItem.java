package com.aregcraft.reforging.target;

import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.inventory.EquipmentSlot;

public interface TargetItem {
    boolean isIngredient(ItemWrapper item);

    void addAttributeModifiers(ItemWrapper item);

    EquipmentSlot getSlot();

    default double getAttackDamage() {
        return 0;
    }

    default double getAttackSpeed() {
        return 0;
    }

    default double getArmor() {
        return 0;
    }

    default double getArmorToughness() {
        return 0;
    }
}
