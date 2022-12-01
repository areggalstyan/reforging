package com.aregcraft.reforging.reforge;

import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.ability.base.BaseAbility;
import com.aregcraft.reforging.format.Format;
import com.aregcraft.reforging.item.ItemStackWrapper;
import com.aregcraft.reforging.item.Weapon;
import com.aregcraft.reforging.util.Named;
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
        return apply(ItemStackWrapper.wrap(item));
    }

    public boolean apply(ItemStackWrapper item) {
        if (!Weapon.has(item)) {
            return false;
        }
        if (item.has("reforge")) {
            var name = item.get("reforge");
            if (Reforging.config().reforges().containsKey(name)) {
                removeModifiers(item, name);
                removeLore(item, name);
            }
        }
        if (!item.has("display_name")) {
            item.set("display_name", item.hasDisplayName() ? item.displayName() : Weapon.of(item).displayName());
        }
        item.set("reforge", name);
        addModifiers(item);
        item.displayName(displayNameFormat(item).format(Reforging.config().item().name()));
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

    private Format loreFormat(ItemStackWrapper item) {
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

    private Format displayNameFormat(ItemStackWrapper item) {
        return Format.builder()
                .entry("REFORGE_NAME", name)
                .entry("NAME", item.get("display_name"))
                .build();
    }

    private void removeModifiers(ItemStackWrapper item, String name) {
        item.removeModifier(Attribute.GENERIC_MAX_HEALTH, name);
        item.removeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, name);
        item.removeModifier(Attribute.GENERIC_MOVEMENT_SPEED, name);
        item.removeModifier(Attribute.GENERIC_ATTACK_DAMAGE, name);
        item.removeModifier(Attribute.GENERIC_ARMOR, name);
        item.removeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, name);
        item.removeModifier(Attribute.GENERIC_ATTACK_SPEED, name);
        item.removeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, name);
    }

    private void addModifiers(ItemStackWrapper item) {
        item.addModifier(Attribute.GENERIC_MAX_HEALTH, name, maxHealth);
        item.addModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, name, knockbackResistance);
        item.addModifier(Attribute.GENERIC_MOVEMENT_SPEED, name, movementSpeed);
        item.addModifier(Attribute.GENERIC_ATTACK_DAMAGE, name, Weapon.of(item).damage() + attackDamage);
        item.addModifier(Attribute.GENERIC_ARMOR, name, armor);
        item.addModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, name, armorToughness);
        item.addModifier(Attribute.GENERIC_ATTACK_SPEED, name, Weapon.of(item).speed() - 4 + attackSpeed);
        item.addModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, name, attackKnockback);
        item.flags(ItemFlag.HIDE_ATTRIBUTES);
    }

    private void removeLore(ItemStackWrapper item, String name) {
        var lore = new ArrayList<>(item.lore());
        Reforging.config().item().lore().stream().map(Reforging.config().reforges().get(name).loreFormat(item)::format)
                .filter(lore::contains).mapToInt(lore::lastIndexOf).forEach(lore::remove);
        item.lore(lore);
    }

    private void addLore(ItemStackWrapper item) {
        var lore = new ArrayList<>(item.lore());
        Reforging.config().item().lore().stream().filter(this::isDisplayable)
                .map(loreFormat(item)::format).forEach(lore::add);
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
        var item = ItemStackWrapper.wrap(rawItem);
        if (item == null || !item.has("reforge")) {
            return;
        }
        var reforge = Reforging.config().reforges().get(item.get("reforge"));
        if (reforge != null && reforge.ability != null) {
            reforge.ability.activate(player);
        }
    }
}
