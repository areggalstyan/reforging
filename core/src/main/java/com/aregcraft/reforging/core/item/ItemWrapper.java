package com.aregcraft.reforging.core.item;

import com.aregcraft.reforging.core.Wrapper;
import com.aregcraft.reforging.core.data.Data;
import com.aregcraft.reforging.core.data.DataHolder;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemWrapper implements Wrapper<ItemStack>, DataHolder {
    protected final ItemStack self;
    protected final ItemMeta meta;

    protected ItemWrapper(ItemStack self) {
        this.self = self;
        meta = Objects.requireNonNull(self.getItemMeta());
    }

    public static ItemWrapper create() {
        return create(Material.AIR);
    }

    public static ItemWrapper create(Material material) {
        return wrap(new ItemStack(material));
    }

    public static ItemWrapper wrap(ItemStack self) {
        if (self == null) {
            return null;
        }
        return new ItemWrapper(self);
    }

    public Material material() {
        return self.getType();
    }

    public void material(Material material) {
        self.setType(material);
    }

    public int amount() {
        return self.getAmount();
    }

    public void amount(int amount) {
        self.setAmount(amount);
    }

    public boolean hasName() {
        return meta.hasDisplayName();
    }

    public String name() {
        return meta.getDisplayName();
    }

    public String nameOrElse(String other) {
        return hasName() ? name() : other;
    }

    public void name(String name) {
        meta.setDisplayName(name);
        self.setItemMeta(meta);
    }

    public boolean hasLocalizedName() {
        return meta.hasLocalizedName();
    }

    public String localizedName() {
        return meta.getLocalizedName();
    }

    public String localizedNameOrElse(String other) {
        return hasLocalizedName() ? localizedName() : other;
    }

    public void localizedName(String localizedName) {
        meta.setLocalizedName(localizedName);
        self.setItemMeta(meta);
    }

    public List<String> lore() {
        return nullSafeList(meta.getLore());
    }

    public void lore(List<String> lore) {
        meta.setLore(lore);
        self.setItemMeta(meta);
    }

    public void lore(String... lore) {
        lore(List.of(lore));
    }

    public boolean hasEnchants() {
        return meta.hasEnchants();
    }

    public boolean hasEnchant(Enchantment enchant) {
        return meta.hasEnchant(enchant);
    }

    public int enchantLevel(Enchantment enchant) {
        return meta.getEnchantLevel(enchant);
    }

    public Map<Enchantment, Integer> enchants() {
        return meta.getEnchants();
    }

    public void addEnchant(Enchantment enchant, int level) {
        meta.addEnchant(enchant, level, true);
        self.setItemMeta(meta);
    }

    public void removeEnchant(Enchantment enchant) {
        meta.removeEnchant(enchant);
        self.setItemMeta(meta);
    }

    public boolean hasConflictingEnchant(Enchantment enchant) {
        return meta.hasConflictingEnchant(enchant);
    }

    public boolean hasFlag(ItemFlag flag) {
        return meta.hasItemFlag(flag);
    }

    public void addFlags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        self.setItemMeta(meta);
    }

    public void removeFlags(ItemFlag... flags) {
        meta.removeItemFlags(flags);
        self.setItemMeta(meta);
    }

    public Set<ItemFlag> flags() {
        return meta.getItemFlags();
    }

    public boolean isUnbreakable() {
        return meta.isUnbreakable();
    }

    public void makeUnbreakable() {
        meta.setUnbreakable(true);
        self.setItemMeta(meta);
    }

    public void makeBreakable() {
        meta.setUnbreakable(false);
        self.setItemMeta(meta);
    }

    public boolean hasModifiers() {
        return meta.hasAttributeModifiers();
    }

    public Multimap<Attribute, AttributeModifier> modifiers() {
        return nullSafeMultimap(meta.getAttributeModifiers());
    }

    public Multimap<Attribute, AttributeModifier> modifiers(EquipmentSlot slot) {
        return nullSafeMultimap(meta.getAttributeModifiers(slot));
    }

    public List<AttributeModifier> modifiers(Attribute attribute) {
        return nullSafeList(meta.getAttributeModifiers(attribute));
    }

    public void modifiers(Multimap<Attribute, AttributeModifier> modifiers) {
        meta.setAttributeModifiers(modifiers);
        self.setItemMeta(meta);
    }

    public void addModifier(Attribute attribute, AttributeModifier modifier) {
        meta.addAttributeModifier(attribute, modifier);
        self.setItemMeta(meta);
    }

    public void removeModifier(Attribute attribute, AttributeModifier modifier) {
        meta.removeAttributeModifier(attribute, modifier);
        self.setItemMeta(meta);
    }

    public void removeModifier(Attribute attribute) {
        meta.removeAttributeModifier(attribute);
        self.setItemMeta(meta);
    }

    public void removeModifier(EquipmentSlot slot) {
        meta.removeAttributeModifier(slot);
        self.setItemMeta(meta);
    }

    public ModifierBuilder modifier() {
        return new ModifierBuilder(this);
    }

    @Override
    public ItemStack unwrap() {
        return self;
    }

    @Override
    public String toString() {
        return self.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Objects.equals(self,  ((ItemWrapper) o).self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(self);
    }

    private <E> List<E> nullSafeList(Collection<E> collection) {
        return new ArrayList<>(Optional.ofNullable(collection).orElseGet(Collections::emptyList));
    }

    private <K, V> Multimap<K, V> nullSafeMultimap(Multimap<K, V> multimap) {
        return Optional.ofNullable(multimap).orElseGet(ImmutableMultimap::of);
    }

    @Override
    public Data data() {
        return Data.of(meta);
    }

    public static class ModifierBuilder {
        private final ItemWrapper item;
        private Attribute attribute;
        private UUID uuid;
        private String name;
        private double amount;
        private AttributeModifier.Operation operation;
        private EquipmentSlot slot;

        private ModifierBuilder(ItemWrapper item) {
            this.item = item;
            uuid(UUID.randomUUID());
            amount(0);
            operation(AttributeModifier.Operation.ADD_NUMBER);
        }

        public ModifierBuilder attribute(Attribute attribute) {
            this.attribute = attribute;
            name = Optional.ofNullable(name).orElseGet(attribute::name);
            return this;
        }

        public ModifierBuilder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public ModifierBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ModifierBuilder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public ModifierBuilder operation(AttributeModifier.Operation operation) {
            this.operation = operation;
            return this;
        }

        public ModifierBuilder slot(EquipmentSlot slot) {
            this.slot = slot;
            return this;
        }

        public void add() {
            item.addModifier(attribute, new AttributeModifier(uuid, name, amount, operation, slot));
        }

        public void remove() {
            item.modifiers(attribute).stream().filter(it -> it.getName().equals(name))
                    .forEach(it -> item.removeModifier(attribute, it));
        }
    }
}