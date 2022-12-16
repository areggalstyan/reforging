package com.aregcraft.reforging.plugin.reforge;

import com.aregcraft.reforging.plugin.Reforging;
import com.aregcraft.reforging.plugin.ability.base.BaseAbility;
import com.aregcraft.reforging.core.Format;
import com.aregcraft.reforging.core.item.ItemWrapper;
import com.aregcraft.reforging.plugin.Weapon;
import com.aregcraft.reforging.core.Named;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Reforge implements Named, Listener {
    private final String name;
    private final BaseAbility ability;
    private final float maxHealth;
    private final float knockbackResistance;
    private final float movementSpeed;
    private final float attackDamage;
    private final float armor;
    private final float armorToughness;
    private final float attackSpeed;
    private final float attackKnockback;

    public Reforge() {
        name = "";
        ability = BaseAbility.NONE;
        maxHealth = 0;
        knockbackResistance = 0;
        movementSpeed = 0;
        attackDamage = 0;
        armor = 0;
        armorToughness = 0;
        attackSpeed = 0;
        attackKnockback = 0;
        Bukkit.getPluginManager().registerEvents(this, Reforging.plugin());
    }

    public String name() {
        return name;
    }

    public boolean apply(ItemStack item) {
        return apply(ItemWrapper.wrap(item));
    }

    public boolean apply(ItemWrapper item) {
        if (!Weapon.has(item)) {
            return false;
        }
        if (item.data().has("reforge", PersistentDataType.STRING)) {
            var name = item.data().get("reforge", PersistentDataType.STRING);
            if (Reforging.config().reforges().containsKey(name)) {
                removeModifiers(item, name);
                removeLore(item, name);
            }
        }
        if (!item.data().has("display_name", PersistentDataType.STRING)) {
            item.data().set("display_name", PersistentDataType.STRING,
                    item.hasName() ? item.name() : Weapon.of(item).displayName());
        }
        item.data().set("reforge", PersistentDataType.STRING, name);
        addModifiers(item);
        item.name(displayNameFormat(item).format(Reforging.config().item().name()));
        addLore(item);
        return true;
    }

    private boolean isDisplayable(String text) {
        return !(text.contains("%MAX_HEALTH%") && maxHealth == 0
                || text.contains("%KNOCKBACK_RESISTANCE%") && knockbackResistance == 0
                || text.contains("%MOVEMENT_SPEED%") && movementSpeed == 0
                || text.contains("%ARMOR%") && armor == 0
                || text.contains("%ARMOR_TOUGHNESS%") && armorToughness == 0
                || text.contains("%ATTACK_KNOCKBACK%") && attackKnockback == 0);
    }

    private Format loreFormat(ItemWrapper item) {
        return Format.builder()
                .entry("ABILITY", ability.name())
                .entry("BASE_ATTACK_SPEED", Weapon.of(item).speed())
                .entry("ATTACK_SPEED", attackSpeed)
                .entry("BASE_ATTACK_DAMAGE", Weapon.of(item).damage())
                .entry("ATTACK_DAMAGE", attackDamage)
                .entry("MAX_HEALTH", maxHealth)
                .entry("KNOCKBACK_RESISTANCE", knockbackResistance)
                .entry("MOVEMENT_SPEED", movementSpeed)
                .entry("ARMOR", armor)
                .entry("ARMOR_TOUGHNESS", armorToughness)
                .entry("ATTACK_KNOCKBACK", attackKnockback)
                .mapper(Float.class, new DecimalFormat()::format)
                .build();
    }

    private Format displayNameFormat(ItemWrapper item) {
        return Format.builder()
                .entry("REFORGE_NAME", name)
                .entry("NAME", item.data().get("display_name", PersistentDataType.STRING))
                .build();
    }

    private void removeModifiers(ItemWrapper item, String name) {
        removeModifier(item, Attribute.GENERIC_MAX_HEALTH, name);
        removeModifier(item, Attribute.GENERIC_KNOCKBACK_RESISTANCE, name);
        removeModifier(item, Attribute.GENERIC_MOVEMENT_SPEED, name);
        removeModifier(item, Attribute.GENERIC_ATTACK_DAMAGE, name);
        removeModifier(item, Attribute.GENERIC_ARMOR, name);
        removeModifier(item, Attribute.GENERIC_ARMOR_TOUGHNESS, name);
        removeModifier(item, Attribute.GENERIC_ATTACK_SPEED, name);
        removeModifier(item, Attribute.GENERIC_ATTACK_KNOCKBACK, name);
    }

    private void removeModifier(ItemWrapper item, Attribute attribute, String name) {
        item.modifier().attribute(attribute).name(name).remove();
    }

    private void addModifiers(ItemWrapper item) {
        addModifier(item, Attribute.GENERIC_MAX_HEALTH, name, maxHealth);
        addModifier(item, Attribute.GENERIC_KNOCKBACK_RESISTANCE, name, knockbackResistance);
        addModifier(item, Attribute.GENERIC_MOVEMENT_SPEED, name, movementSpeed);
        addModifier(item, Attribute.GENERIC_ATTACK_DAMAGE, name, Weapon.of(item).damage() + attackDamage);
        addModifier(item, Attribute.GENERIC_ARMOR, name, armor);
        addModifier(item, Attribute.GENERIC_ARMOR_TOUGHNESS, name, armorToughness);
        addModifier(item, Attribute.GENERIC_ATTACK_SPEED, name, Weapon.of(item).speed() - 4 + attackSpeed);
        addModifier(item, Attribute.GENERIC_ATTACK_KNOCKBACK, name, attackKnockback);
        item.addFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    private void addModifier(ItemWrapper item, Attribute attribute, String name, float amount) {
        item.modifier().attribute(attribute).name(name).amount(amount).add();
    }

    private void removeLore(ItemWrapper item, String name) {
        var lore = new ArrayList<>(item.lore());
        Reforging.config().item().lore().stream()
                .map(Reforging.config().reforges().get(name).loreFormat(item)::format)
                .filter(lore::contains)
                .mapToInt(lore::lastIndexOf)
                .forEach(lore::remove);
        item.lore(lore);
    }

    private void addLore(ItemWrapper item) {
        var lore = new ArrayList<>(item.lore());
        Reforging.config().item().lore().stream()
                .filter(this::isDisplayable)
                .map(loreFormat(item)::format)
                .forEach(lore::add);
        item.lore(lore);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            activateAbility(event.getItem(), event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        var player = event.getPlayer();
        activateAbility(player.getInventory().getItemInMainHand(), player);
    }

    private void activateAbility(ItemStack rawItem, Player player) {
        var item = ItemWrapper.wrap(rawItem);
        if (item == null || !item.data().has("reforge", PersistentDataType.STRING)) {
            return;
        }
        var reforge = Reforging.config().reforges().get(item.data().get("reforge",
                PersistentDataType.STRING));
        if (reforge != null && reforge.ability != null) {
            reforge.ability.activate(player);
        }
    }
}
