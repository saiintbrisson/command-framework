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
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
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
