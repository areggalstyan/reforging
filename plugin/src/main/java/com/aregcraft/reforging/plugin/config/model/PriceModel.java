package com.aregcraft.reforging.plugin.config.model;

import com.aregcraft.reforging.plugin.annotation.External;

/**
 * Specifies the amount of health and food taken from the player when activating the ability.
 * @param health Specifies the amount of health taken from the player when activating the ability.
 * @param food Specifies the amount of food taken from the player when activating the ability.
 */
@External
public record PriceModel(double health, int food) {
}
