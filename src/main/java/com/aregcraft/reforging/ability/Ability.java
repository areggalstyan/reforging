package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.data.Price;
import org.bukkit.entity.Player;

public abstract class Ability {
    public static final Ability DUMMY = new Ability() {
        @Override
        public void activate(Player player) {
        }
    };

    public Price price;

    public abstract void activate(Player player);

    protected void charge(Player player) {
        player.damage(price.health);
        player.setFoodLevel(Math.max(player.getFoodLevel() - price.food, 0));
    }
}
