package me.saiintbrisson.minecraft.command.argument;

import java.util.HashMap;

/**
 * @author SaiintBrisson
 */
public class AdapterMap extends HashMap<Class<?>, TypeAdapter<?>> {
    public AdapterMap(boolean registerDefault) {
        super();

        if (!registerDefault) {
            return;
        }

        put(String.class, String::valueOf);
        put(Character.class, argument -> argument.charAt(0));
        put(Integer.class, Integer::valueOf);
        put(Double.class, Double::valueOf);
        put(Float.class, Float::valueOf);
        put(Long.class, Long::valueOf);
        put(Boolean.class, Boolean::valueOf);
        put(Byte.class, Byte::valueOf);

        put(Character.TYPE, argument -> argument.charAt(0));
        put(Integer.TYPE, Integer::parseInt);
        put(Double.TYPE, Double::parseDouble);
        put(Float.TYPE, Float::parseFloat);
        put(Long.TYPE, Long::parseLong);
        put(Boolean.TYPE, Boolean::parseBoolean);
        put(Byte.TYPE, Byte::parseByte);
    }

    public <T> TypeAdapter<T> put(Class<T> key, TypeAdapter<T> value) {
        return (TypeAdapter<T>) super.put(key, value);
    }
}
