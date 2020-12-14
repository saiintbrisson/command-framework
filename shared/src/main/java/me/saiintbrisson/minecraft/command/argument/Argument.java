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

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
 */

@Getter
@Builder
public class Argument<T> {
    private final String name;

    private final Class<T> type;
    private final TypeAdapter<T> adapter;

    private final T defaultValue;

    private final boolean isNullable;
    private final boolean isArray;

    public Argument(@NonNull String name,
                    @NonNull Class<T> type, TypeAdapter<T> adapter,
                    T defaultValue,
                    boolean isNullable, boolean isArray) {
        this.name = name;
        this.type = type;
        this.adapter = adapter;
        this.defaultValue = defaultValue;
        this.isNullable = isNullable;
        this.isArray = isArray;
    }
}
