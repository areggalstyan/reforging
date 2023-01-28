package com.aregcraft.reforging.meta;

import com.aregcraft.delta.meta.MetaDoclet;
import com.aregcraft.delta.meta.replacement.AbilityReplacement;
import com.aregcraft.delta.meta.replacement.BaseReplacement;
import com.aregcraft.delta.meta.replacement.JsonReplacement;

public class ReforgingMeta extends MetaDoclet {
    public ReforgingMeta() {
        super(new JsonReplacement("abilities"),
                new JsonReplacement("item"),
                new JsonReplacement("reforge_stones"),
                new JsonReplacement("reforges"),
                new JsonReplacement("reforging_anvil"),
                new AbilityReplacement("abilities"),
                new BaseReplacement("bases"));
    }
}
