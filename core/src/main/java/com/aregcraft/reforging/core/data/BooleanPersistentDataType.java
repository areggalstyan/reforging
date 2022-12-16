package com.aregcraft.reforging.core.data;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class BooleanPersistentDataType implements PersistentDataType<Byte, Boolean> {
    BooleanPersistentDataType() {
    }

    @Override
    public Class<Byte> getPrimitiveType() {
        return Byte.class;
    }

    @Override
    public Class<Boolean> getComplexType() {
        return Boolean.class;
    }

    @Override
    public Byte toPrimitive(Boolean complex, PersistentDataAdapterContext context) {
        return Objects.equals(complex, true) ? (byte) 1 : (byte) 0;
    }

    @Override
    public Boolean fromPrimitive(Byte primitive, PersistentDataAdapterContext context) {
        return !Objects.equals(primitive, 0);
    }
}
