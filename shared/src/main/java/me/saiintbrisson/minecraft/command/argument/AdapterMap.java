/*
 * Copyright 2020 Luiz Carlos Mourão Paes de Carvalho
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.saiintbrisson.minecraft.command.argument;

import java.util.HashMap;

/**
 * The AdapterMap contains instructions on how to
 * convert strings to java types.
 *
 * @author Luiz Carlos Carvalho
 */
public class AdapterMap extends HashMap<Class<?>, TypeAdapter<?>> {
    /**
     * Creates a new AdapterMap, with default
     * adapters registered.
     */
    public AdapterMap() {
        this(true);
    }

    /**
     * Creates a new AdapterMap.
     *
     * @param registerDefault whether to create the
     *                        map with default adapters
     *                        registered.
     */
    public AdapterMap(boolean registerDefault) {
        super();

        if (registerDefault) {
            put(String.class, String::valueOf);
            put(Integer.class, Integer::valueOf);
            put(Double.class, Double::valueOf);
            put(Float.class, Float::valueOf);
            put(Long.class, Long::valueOf);
            put(Boolean.class, Boolean::valueOf);
            put(Byte.class, Byte::valueOf);

            put(Integer.TYPE, Integer::parseInt);
            put(Double.TYPE, Double::parseDouble);
            put(Float.TYPE, Float::parseFloat);
            put(Long.TYPE, Long::parseLong);
            put(Boolean.TYPE, Boolean::parseBoolean);

            put(Character.TYPE, argument -> argument.charAt(0));
            put(Byte.TYPE, Byte::parseByte);
        }
    }

    /**
     * Registers a new adapter.
     *
     * <pre>{@code
     * AdapterMap map = new AdapterMap();
     * map.put(Integer.class, (string) -> {
     *     return Integer.valueOf(string) * 10;
     * });
     * }</pre>
     *
     * @param key the target class.
     * @param map the mapping function.
     * @param <T> the target type.
     * @return the adapter created.
     */
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> put(Class<T> key, TypeAdapter<T> map) {
        return (TypeAdapter<T>) super.put(key, map);
    }
}
