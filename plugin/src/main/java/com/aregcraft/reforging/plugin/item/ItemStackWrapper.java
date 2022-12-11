package com.aregcraft.reforging.plugin.item;

import com.aregcraft.reforging.plugin.Reforging;
import com.aregcraft.reforging.plugin.config.model.ItemModel;
import com.aregcraft.reforging.plugin.format.Format;
import com.aregcraft.reforging.plugin.util.QuadConsumer;
import com.aregcraft.reforging.plugin.util.TriConsumer;
import com.aregcraft.reforging.plugin.util.Wrapper;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ItemStackWrapper implements Wrapper<ItemStack> {
    private final ItemStack self;

    private ItemStackWrapper(ItemStack self) {
        this.self = self;
    }

    public static ItemStackWrapper wrap(ItemStack self) {
        if (self == null || self.getItemMeta() == null) {
            return null;
        }
        return new ItemStackWrapper(self);
    }

    public static ItemStackWrapper fromModel(Material material, ItemModel model, String id) {
        var item = ItemStackWrapper.create(material);
        item.displayName(Format.DEFAULT.format(model.name()));
        item.lore(model.lore().stream().map(Format.DEFAULT::format).toList());
        item.set("id", id);
        return item;
    }

    public static ItemStackWrapper create(Material material) {
        return wrap(new ItemStack(material));
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

    public boolean hasDisplayName() {
        return fetch(ItemMeta::hasDisplayName);
    }

    public String displayName() {
        return fetch(ItemMeta::getDisplayName);
    }

    public void displayName(String name) {
        edit(ItemMeta::setDisplayName, name);
    }

    public List<String> lore() {
        return Optional.ofNullable(fetch(ItemMeta::getLore)).orElseGet(Collections::emptyList);
    }

    public void lore(List<String> lore) {
        edit(ItemMeta::setLore, lore);
    }

    public void lore(String... lore) {
        lore(List.of(lore));
    }

    public Set<ItemFlag> flags() {
        return fetch(ItemMeta::getItemFlags);
    }

    public void flags(List<ItemFlag> flags) {
        edit(ItemMeta::addItemFlags, flags.toArray(ItemFlag[]::new));
    }

    public void flags(ItemFlag... flags) {
        flags(List.of(flags));
    }

    public Collection<AttributeModifier> modifiers(Attribute attribute) {
        return Optional.ofNullable(fetch(ItemMeta::getAttributeModifiers, attribute))
                .orElseGet(Collections::emptyList);
    }

    public Collection<AttributeModifier> modifiers(Attribute attribute, String name) {
        return modifiers(attribute).stream().filter(it -> it.getName().equals(name)).toList();
    }

    public void addModifier(Attribute attribute, String name, double value) {
        addModifier(attribute, new AttributeModifier(UUID.randomUUID(), name, value,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
    }

    public void addModifier(Attribute attribute, AttributeModifier modifier) {
        edit(ItemMeta::addAttributeModifier, attribute, modifier);
    }

    public void removeModifier(Attribute attribute, String name) {
        modifiers(attribute, name).forEach(it -> edit(ItemMeta::removeAttributeModifier, attribute, it));
    }

    public boolean has(String name) {
        return fetch(ItemMeta::getPersistentDataContainer).has(Reforging.key(name), PersistentDataType.STRING);
    }

    public String get(String name) {
        return fetch(ItemMeta::getPersistentDataContainer).get(Reforging.key(name), PersistentDataType.STRING);
    }

    public void set(String name, String value) {
        edit(ItemMeta::getPersistentDataContainer, PersistentDataContainer::set, Reforging.key(name),
                PersistentDataType.STRING, value);
    }

    public void addEffect(PotionEffectType type, int duration, int amplifier) {
        edit(PotionMeta.class::cast, PotionMeta::addCustomEffect, new PotionEffect(type, duration, amplifier),
                true);
    }

    private <T> void edit(BiConsumer<ItemMeta, T> action, T t) {
        var itemMeta = self.getItemMeta();
        action.accept(itemMeta, t);
        self.setItemMeta(itemMeta);
    }

    private <T, U> void edit(TriConsumer<ItemMeta, T, U> action, T t, U u) {
        var itemMeta = self.getItemMeta();
        action.accept(itemMeta, t, u);
        self.setItemMeta(itemMeta);
    }

    private <T, U, V> void edit(Function<ItemMeta, T> mapper, TriConsumer<T, U, V> action, U u, V v) {
        var itemMeta = self.getItemMeta();
        action.accept(mapper.apply(itemMeta), u, v);
        self.setItemMeta(itemMeta);
    }

    private <T, U, V, W> void edit(Function<ItemMeta, T> mapper, QuadConsumer<T, U, V, W> action, U u, V v, W w) {
        var itemMeta = self.getItemMeta();
        action.accept(mapper.apply(itemMeta), u, v, w);
        self.setItemMeta(itemMeta);
    }

    private <T> T fetch(Function<ItemMeta, T> fetcher) {
        return fetcher.apply(self.getItemMeta());
    }

    private <T, U> T fetch(BiFunction<ItemMeta, U, T> fetcher, U u) {
        return fetcher.apply(self.getItemMeta(), u);
    }

    @Override
    public ItemStack unwrap() {
        return self;
    }
}
