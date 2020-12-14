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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.argument.Argument;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exception.CommandException;
import me.saiintbrisson.minecraft.command.message.MessageType;
import me.saiintbrisson.minecraft.command.util.ArrayUtil;
import me.saiintbrisson.minecraft.command.util.StringUtil;

import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@RequiredArgsConstructor
public class ArgumentEvaluator<S> {
    private final List<Argument<?>> argumentList;

    public Object[] parseArguments(Context<S> context) {
        Object[] parameters = new Object[0];
        AtomicInteger currentArg = new AtomicInteger(0);

        for (Argument<?> argument : argumentList) {
            if (Context.class.isAssignableFrom(argument.getType())) {
                parameters = ArrayUtil.add(parameters, context);
                continue;
            }

            String arg = readFullString(currentArg, context);
            if (arg == null) {
                if (!argument.isNullable()) {
                    throw new CommandException(MessageType.INCORRECT_USAGE, null);
                } else {
                    parameters = ArrayUtil.add(parameters, argument.getDefaultValue());
                }

                currentArg.incrementAndGet();
                continue;
            }

            Object object;

            if (argument.isArray()) {
                object = Array.newInstance(argument.getType(), 0);

                do {

                    object = ArrayUtil.add(
                      (Object[]) object,
                      argument.getAdapter().convertNonNull(arg)
                    );

                } while ((arg = readFullString(currentArg, context)) != null);

            } else {
                object = argument.getAdapter().convertNonNull(arg);
            }

            parameters = ArrayUtil.add(parameters, object);
        }

        return parameters;
    }

    private String readFullString(AtomicInteger currentArg, Context<S> context) {
        String arg = context.getArg(currentArg.get());
        if (arg == null) {
            return null;
        }

        currentArg.incrementAndGet();

        if (arg.charAt(0) == '"') {
            StringBuilder builder = new StringBuilder(arg.substring(1));

            while ((arg = context.getArg(currentArg.get())) != null) {
                builder.append(" ");
                currentArg.incrementAndGet();

                final int length = arg.length();

                if (arg.charAt(length - 1) == '"' && (length == 1 || arg.charAt(length - 2) != '\\')) {
                    builder.append(arg, 0, length - 1);
                    break;
                }

                builder.append(arg);
            }

            return builder.toString().replace("\\\"", "\"");
        } else {
            return arg;
        }
    }


    public String buildUsage(String name) {
        StringBuilder builder = new StringBuilder(name);

        for (Argument<?> argument : argumentList) {
            if (Context.class.isAssignableFrom(argument.getType())) {
                continue;
            }

            builder.append(argument.isNullable() ? " [" : " <");

            builder.append(StringUtil.uncapitalize(argument.getType().getSimpleName()));
            if (argument.isArray()) {
                builder.append("...");
            }

            builder.append(argument.isNullable() ? "]" : ">");
        }

        return builder.toString();
    }
}
