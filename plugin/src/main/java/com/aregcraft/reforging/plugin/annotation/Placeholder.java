package com.aregcraft.reforging.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Repeatable(Placeholders.class)
@Target(ElementType.FIELD)
public @interface Placeholder {
    String name();
    String description();
}
