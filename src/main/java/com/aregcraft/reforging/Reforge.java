package com.aregcraft.reforging;

import com.aregcraft.delta.api.FormattingContext;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.delta.api.registry.Identifiable;
import com.aregcraft.reforging.ability.Ability;
import com.aregcraft.reforging.target.Target;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;

public class Reforge implements Identifiable<String>, Listener {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    private final String id;
    private final String name;
    private final Map<Attribute, Double> attributes;
    private final Ability ability;
    private final Set<Target> targets;

    public Reforge(String id, String name, Map<Attribute, Double> attributes, Ability ability, Set<Target> targets) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
        this.ability = ability;
        this.targets = targets;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Target> getTargets() {
        return targets;
    }

    public void activateAbility(Player player) {
        if (ability != null) {
            ability.activate(player);
        }
    }

    public boolean isApplicable(ItemWrapper item) {
        return targets.stream().anyMatch(it -> it.matches(item));
    }

    public ItemWrapper apply(Player player, ItemWrapper item, Reforging plugin) {
        var persistentData = item.getPersistentData(plugin);
        persistentData.remove("cooldown");
        persistentData.setIfAbsent("name", item.getNameOrElse(plugin.getDefaultName(player, item)));
        var display = Target.isWeapon(item) ? plugin.getWeaponDisplay() : plugin.getArmorDisplay();
        if (persistentData.has("reforge", String.class)) {
            var reforge = persistentData.get("reforge", String.class);
            item.setFormattingContext(plugin.getReforges().findAny(reforge)
                    .getFormattingContext(item, player, plugin));
            item.removeLore(display.lore());
            item.removeAttributeModifiers(reforge);
        } else {
            item.addFlags(ItemFlag.HIDE_ATTRIBUTES);
            Target.addAttributeModifiers(item);
        }
        persistentData.set("reforge", id);
        item.setFormattingContext(getFormattingContext(item, player, plugin));
        item.appendDisplay(display);
        attributes.forEach((attribute, amount) -> addAttributeModifier(item, attribute, amount));
        return item;
    }

    private void addAttributeModifier(ItemWrapper item, Attribute attribute, double amount) {
        item.createAttributeModifierBuilder()
                .attribute(attribute)
                .name(id)
                .amount(amount)
                .slot(Target.getSlot(item))
                .add();
    }

    public FormattingContext getFormattingContext(ItemWrapper item, Player player, Reforging plugin) {
        var builder = FormattingContext.builder()
                .placeholder("name", item.getPersistentData(plugin).get("name", String.class))
                .placeholder("reforgeName", name)
                .placeholder("slot", plugin.getSlotName(player, Target.getSlot(item)))
                .formatter(Double.class, DECIMAL_FORMAT::format);
        attributes.forEach((attribute, amount) -> builder.placeholder(attribute.name().toLowerCase(), amount));
        if (Target.isWeapon(item)) {
            builder.placeholder("base_attack_damage", Target.getAttackDamage(item))
                    .placeholder("base_attack_speed", Target.getAttackSpeed(item));
        } else if (Target.isArmor(item)) {
            builder.placeholder("base_armor", Target.getArmor(item))
                    .placeholder("base_armor_toughness", Target.getArmorToughness(item));
        }
        if (ability != null) {
            builder.placeholder("ability", ability.getName())
                    .placeholder("abilityDescription", ability.getDescription())
                    .placeholder("cooldown", ability.getCooldown() / 20.0);
            var priceHealth = ability.getPriceHealth();
            if (priceHealth > 0) {
                builder.placeholder("priceHealth", ability.getPriceHealth());
            }
            var priceFood = ability.getPriceFood();
            if (priceFood > 0) {
                builder.placeholder("priceFood", ability.getPriceFood());
            }
        }
        if (Target.isWeapon(item) && !attributes.containsKey(Attribute.GENERIC_ATTACK_DAMAGE)) {
            builder.placeholder("generic_attack_damage", 0);
        }
        if (Target.isWeapon(item) && !attributes.containsKey(Attribute.GENERIC_ATTACK_SPEED)) {
            builder.placeholder("generic_attack_speed", 0);
        }
        if (Target.isArmor(item) && !attributes.containsKey(Attribute.GENERIC_ARMOR)) {
            builder.placeholder("generic_armor", 0);
        }
        if (Target.isArmor(item) && !attributes.containsKey(Attribute.GENERIC_ARMOR_TOUGHNESS)) {
            builder.placeholder("generic_armor_toughness", 0);
        }
        return builder.build();
    }
}
