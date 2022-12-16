package com.aregcraft.reforging.core.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class PotionItemWrapper extends ItemWrapper {
    private final PotionMeta potionMeta;

    protected PotionItemWrapper(ItemStack self) {
        super(self);
        potionMeta = (PotionMeta) meta;
    }

    public static PotionItemWrapper create() {
        return create(Material.AIR);
    }

    public static PotionItemWrapper create(Material material) {
        return wrap(new ItemStack(material));
    }

    public static PotionItemWrapper wrap(ItemStack self) {
        if (self == null) {
            return null;
        }
        return new PotionItemWrapper(self);
    }

    public static PotionItemWrapper wrap(ItemWrapper item) {
        if (item == null) {
            return null;
        }
        return wrap(item.self);
    }

    public void addEffect(PotionEffect effect) {
        potionMeta.addCustomEffect(effect, true);
        self.setItemMeta(potionMeta);
    }
}
