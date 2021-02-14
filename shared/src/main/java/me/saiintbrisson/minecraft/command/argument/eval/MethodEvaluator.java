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

package me.saiintbrisson.minecraft.command.argument.eval;

import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.argument.AdapterMap;
import me.saiintbrisson.minecraft.command.argument.Argument;
import me.saiintbrisson.minecraft.command.argument.TypeAdapter;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exception.NoSuchConverterException;
import me.saiintbrisson.minecraft.command.util.ArrayUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MethodEvaluator {
    private final AdapterMap adapterMap;

    public List<Argument<?>> evaluateMethod(Method method) {
        final List<Argument<?>> argumentList = new ArrayList<>();

        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class type = parameter.getType();
            boolean isArray = type.isArray();

            final Argument.ArgumentBuilder builder = Argument
              .builder()
              .name(parameter.getName())
              .type(type)
              .isArray(isArray);

            if (Context.class.isAssignableFrom(type)) {
                argumentList.add(builder.build());
                continue;
            }

            if (isArray) {
                if (i != parameters.length - 1) {
                    throw new IllegalArgumentException("Arrays must be the last parameter in a command, "
                      + method.getName());
                }

                builder.type(type = type.getComponentType());
            }

            builder.adapter(adapterMap.get(type));

            final Optional optional = parameter.getDeclaredAnnotation(Optional.class);
            if (optional == null) {
                argumentList.add(builder.build());
                continue;
            }

            argumentList.add(createOptional(method, type, isArray, optional.def(), builder));
        }

        return argumentList;
    }

    private Argument createOptional(Method method, Class type, boolean isArray,
                                    String[] def, Argument.ArgumentBuilder builder) {
        if (type.isPrimitive() && def.length == 0) {
            throw new IllegalArgumentException("Use wrappers instead of primitive types for nullability, "
              + method.getName());
        }

        final TypeAdapter<?> adapter = adapterMap.get(type);
        if (adapter == null) {
            throw new NoSuchConverterException(type);
        }

        builder.isNullable(true);

        if (isArray && def.length != 0) {
            builder.defaultValue(createArray(type, adapter, def));
        } else if (def.length != 0) {
            builder.defaultValue(adapter.convertNonNull(def[0]));
        }

        return builder.build();
    }

    private Object[] createArray(Class type, TypeAdapter<?> adapter, String[] def) {
        Object[] value = (Object[]) Array.newInstance(type, 0);

        for (String s : def) {
            value = ArrayUtil.add(
              value,
              adapter.convertNonNull(s)
            );
        }

        return value;
    }
}
