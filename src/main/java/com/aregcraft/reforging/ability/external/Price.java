package com.aregcraft.reforging.ability.external;

import com.aregcraft.reforging.annotation.External;

/**
 * Specifies the price the player pays when activates this ability.
 */
@External
public class Price {
    /**
     * Specifies the amount of health subtracted from the player when activating the ability.
     */
    public double health;
    /**
     * Specifies the amount of food subtracted from the player when activating the ability.
     */
    public int food;
}
