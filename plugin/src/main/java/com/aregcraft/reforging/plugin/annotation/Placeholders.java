package com.aregcraft.reforging.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface Placeholders {
    Placeholder[] value();
}
