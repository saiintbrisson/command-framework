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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * The model for each argument in the command.
 * <p>It contains the main information of each argument
 * such as it's type and name.</p>
 *
 * @author Luiz Carlos Mourão
 */
@Getter
@Builder
@AllArgsConstructor
public class Argument<T> {
    private final String name;

    private final Class<T> type;
    private final TypeAdapter<T> adapter;

    private final T defaultValue;

    private final boolean isNullable;
    private final boolean isArray;
    private final boolean ignoreQuote;
}
