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

package me.saiintbrisson.minecraft.command;

import me.saiintbrisson.minecraft.command.argument.AdapterMap;
import me.saiintbrisson.minecraft.command.argument.TypeAdapter;
import me.saiintbrisson.minecraft.command.argument.eval.MethodEvaluator;
import me.saiintbrisson.minecraft.command.command.CommandHolder;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.executor.CommandExecutor;
import me.saiintbrisson.minecraft.command.executor.CompleterExecutor;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
 */
public interface CommandFrame<P, S, C extends CommandHolder<S, ? extends C>> {
    P getPlugin();
    AdapterMap getAdapterMap();
    MessageHolder getMessageHolder();

    Map<String, C> getCommandMap();
    MethodEvaluator getMethodEvaluator();

    @Nullable
    Executor getExecutor();

    C getCommand(String name);

    default <T> void registerAdapter(Class<T> type, TypeAdapter<T> adapter) {
        getAdapterMap().put(type, adapter);
    }

    void registerCommands(Object... objects);
    void registerCommand(CommandInfo commandInfo, CommandExecutor<S> commandExecutor);
    void registerCompleter(String name, CompleterExecutor<S> completerExecutor);

    boolean unregisterCommand(String name);
}
