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

package me.saiintbrisson.minecraft.command.parameter;

import me.saiintbrisson.minecraft.command.parameter.interfaces.Extractor;

import java.util.HashMap;

/**
 * The AdapterMap contains instructions on how to
 * convert strings to java types.
 *
 * @author Luiz Carlos Carvalho
 */
public final class ExtractorMap extends HashMap<Class<?>, Extractor<?>> {
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
    public <T> Extractor<T> put(Class<T> key, Extractor<T> map) {
        return (Extractor<T>) super.put(key, map);
    }
}
