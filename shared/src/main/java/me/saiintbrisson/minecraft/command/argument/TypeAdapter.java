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

/**
 * @author Luiz Carlos Mourão
 */
public interface TypeAdapter<T> {

    T convert(String raw);

    default T convertNonNull(String raw) {
        final T result = convert(raw);

        if (result == null) {
            throw new NullPointerException();
        }

        return result;
    }

}
