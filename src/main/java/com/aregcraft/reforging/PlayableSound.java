package com.aregcraft.reforging;

import org.bukkit.Sound;
import org.bukkit.block.Block;

public class PlayableSound {
    private final Sound type;
    private final float volume;
    private final float pitch;

    public PlayableSound(Sound type, float volume, float pitch) {
        this.type = type;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void play(Block block) {
        block.getWorld().playSound(block.getLocation(), type, volume, pitch);
    }
}
