/*
 * Copyright 2020 Luiz Carlos Carvalho Paes de Carvalho
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
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.executor.CommandExecutor;
import me.saiintbrisson.minecraft.command.executor.CompleterExecutor;

/**
 * The CommandFrame is the core of the framework,
 * it registers the commands, adapters {@link AdapterMap}.
 *
 * @author Luiz Carlos Carvalho
 */
public interface CommandFrame<P, S> {
    /**
     * Get the plugin that owns this frame instance.
     *
     * @return the plugin that owns this frame instance.
     */
    P plugin();

    /**
     * Get the adapter map used by this frame instance
     * and its commands.
     *
     * @return the adapter map.
     */
    AdapterMap getAdapterMap();

    /**
     * Registers a new adapter.
     *
     * <pre>{@code
     * frame.registerAdapter(Integer.class, (string) -> {
     *     return Integer.valueOf(string) * 10;
     * });
     * }</pre>
     *
     * @param type    the target class.
     * @param adapter the mapping function.
     * @param <T>     the target type.
     */
    default <T> void registerAdapter(Class<T> type, TypeAdapter<T> adapter) {
        getAdapterMap().put(type, adapter);
    }

    /**
     * Registers all public methods annotated with
     * {@link me.saiintbrisson.minecraft.command.annotation.Command Command},
     * {@link me.saiintbrisson.minecraft.command.annotation.Completer Completer},
     * and {@link me.saiintbrisson.minecraft.command.annotation.ExceptionHandler ExceptionHandler}.
     *
     * @param classes the classes to be searched for.
     */
    void registerAll(Object... classes);

    /**
     * Registers a single command with the CommandInfo and Executor
     *
     * @param commandInfo the command metadata.
     * @param executor    the command to be executed.
     */
    void registerCommand(CommandInfo commandInfo, CommandExecutor<S> executor);

    /**
     * Registers a single completer for the given command.
     *
     * @param name     the command name.
     * @param executor the completer to be executed.
     */
    void registerCompleter(String name, CompleterExecutor<S> executor);

    /**
     * Unregisters a command.
     *
     * @param name the command to delete.
     * @return whether the command existed or not.
     */
    boolean unregisterCommand(String name);
}
