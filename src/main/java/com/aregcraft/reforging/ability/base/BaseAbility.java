package com.aregcraft.reforging.ability.base;

import com.aregcraft.reforging.config.model.PriceModel;
import com.aregcraft.reforging.util.Named;
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
