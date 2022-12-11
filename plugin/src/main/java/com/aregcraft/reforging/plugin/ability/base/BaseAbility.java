package com.aregcraft.reforging.plugin.ability.base;

import com.aregcraft.reforging.plugin.config.model.PriceModel;
import com.aregcraft.reforging.plugin.util.Named;
import org.bukkit.entity.Player;

public abstract class BaseAbility implements Named {
    public static final BaseAbility NONE = new BaseAbility() {
        @Override
        protected void perform(Player player) {
        }

        @Override
        public String name() {
            return "NONE";
        }
    };

    /**
     * Specifies the name of the ability.
     */
    private String name;
    protected PriceModel price;

    @Override
    public String name() {
        return name;
    }

    public boolean activate(Player player) {
        perform(player);
        return true;
    }

    protected abstract void perform(Player player);
}
