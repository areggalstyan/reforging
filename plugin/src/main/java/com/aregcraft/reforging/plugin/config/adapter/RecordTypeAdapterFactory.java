package com.aregcraft.reforging.plugin.config.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecordTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        @SuppressWarnings("unchecked")
        var clazz = (Class<T>) type.getRawType();
        if (!clazz.isRecord()) {
            return null;
        }
        var delegate = gson.getDelegateAdapter(this, type);
        return new TypeAdapter<>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                var nameToType = nameToType();
                var names = names();
                var arguments = Arrays.asList(new Object[nameToType.size()]);
                in.beginObject();
                while (in.hasNext()) {
                    var name = in.nextName();
                    arguments.set(names.indexOf(name),
                            gson.getAdapter(TypeToken.get(nameToType.get(name))).read(in));
                }
                in.endObject();
                try {
                    return clazz.getConstructor(types()).newInstance(arguments.toArray(Object[]::new));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                        | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

            private List<String> names() {
                return Arrays.stream(clazz.getRecordComponents()).map(RecordComponent::getName).toList();
            }

            private Class<?>[] types() {
                return Arrays.stream(clazz.getRecordComponents()).map(RecordComponent::getType)
                        .toArray(Class<?>[]::new);
            }

            private Map<String, Type> nameToType() {
                return Arrays.stream(clazz.getRecordComponents()).collect(Collectors.toMap(RecordComponent::getName,
                        RecordComponent::getGenericType));
            }
        };
    }
}
