package com.aregcraft.reforging.ability;

import org.bukkit.entity.Player;

public class Price {
    private final double health;
    private final int food;

    public Price(double health, int food) {
        this.health = health;
        this.food = food;
    }

    public void deduct(Player player) {
        player.damage(health);
        player.setFoodLevel(player.getFoodLevel() - food);
    }
}
