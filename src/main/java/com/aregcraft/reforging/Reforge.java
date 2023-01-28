package com.aregcraft.reforging;

import com.aregcraft.delta.api.FormattingContext;
import com.aregcraft.delta.api.Identifiable;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.reforging.ability.Ability;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;

import java.text.DecimalFormat;
import java.util.Map;

public class Reforge implements Identifiable<String>, Listener {
    private final String id;
    private final String name;
    private final Map<Attribute, Double> attributes;
    private final Ability ability;

    public Reforge(String id, String name, Map<Attribute, Double> attributes, Ability ability) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
        this.ability = ability;
    }

    @Override
    public String getId() {
        return id;
    }

    public void activateAbility(Player player) {
        if (ability != null) {
            ability.activate(player);
        }
    }

    public ItemWrapper apply(Player player, ItemWrapper item, Reforging plugin) {
        var persistentData = item.getPersistentData(plugin);
        persistentData.remove("cooldown");
        persistentData.setIfAbsent("name", item.getNameOrElse(plugin.getDefaultName(player, item)));
        var display = plugin.getItemDisplay();
        if (persistentData.has("reforge", String.class)) {
            var reforge = persistentData.get("reforge", String.class);
            item.setFormattingContext(plugin.getReforge(reforge).getFormattingContext(item, plugin));
            item.removeLore(display.lore());
            item.removeAttributeModifiers(reforge);
        } else {
            item.addFlags(ItemFlag.HIDE_ATTRIBUTES);
            Weapon.of(item).addAttributeModifiers(item);
        }
        persistentData.set("reforge", id);
        item.setFormattingContext(getFormattingContext(item, plugin));
        item.appendDisplay(display);
        attributes.forEach((attribute, amount) -> addAttributeModifier(item, attribute, amount));
        return item;
    }

    private void addAttributeModifier(ItemWrapper item, Attribute attribute, double amount) {
        item.createAttributeModifierBuilder()
                .attribute(attribute)
                .name(id)
                .amount(amount)
                .slot(EquipmentSlot.HAND)
                .add();
    }

    private FormattingContext getFormattingContext(ItemWrapper item, Reforging plugin) {
        var weapon = Weapon.of(item);
        var builder = FormattingContext.builder()
                .placeholder("name", item.getPersistentData(plugin).get("name", String.class))
                .placeholder("reforge_name", name)
                .placeholder("ability", ability == null ? null : ability.getName())
                .placeholder("base_attack_damage", weapon.getAttackDamage())
                .placeholder("base_attack_speed", weapon.getAttackSpeed())
                .formatter(Double.class, new DecimalFormat()::format);
        attributes.forEach((attribute, amount) -> builder.placeholder(attribute.name().toLowerCase(), amount));
        if (!attributes.containsKey(Attribute.GENERIC_ATTACK_DAMAGE)) {
            builder.placeholder("generic_attack_damage", 0);
        }
        if (!attributes.containsKey(Attribute.GENERIC_ATTACK_SPEED)) {
            builder.placeholder("generic_attack_speed", 0);
        }
        return builder.build();
    }
}
