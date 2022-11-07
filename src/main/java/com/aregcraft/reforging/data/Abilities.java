package com.aregcraft.reforging.data;

import com.aregcraft.reforging.ability.EarthAbility;
import com.aregcraft.reforging.ability.FireAbility;
import com.aregcraft.reforging.ability.ShieldAbility;
import com.aregcraft.reforging.ability.StormAbility;
import com.aregcraft.reforging.ability.TeleportAbility;
import com.aregcraft.reforging.ability.ThrowAbility;

import java.util.Map;

public class Abilities {
    public Map<String, ShieldAbility> shieldAbilities;
    public Map<String, FireAbility> fireAbilities;
    public Map<String, ThrowAbility> throwAbilities;
    public Map<String, StormAbility> stormAbilities;
    public Map<String, EarthAbility> earthAbilities;
    public Map<String, TeleportAbility> teleportAbilities;
}
