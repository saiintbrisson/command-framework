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

import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.handlers.CommandHandler;
import me.saiintbrisson.minecraft.command.handlers.CompleterHandler;
import me.saiintbrisson.minecraft.command.parameter.AdapterMap;
import me.saiintbrisson.minecraft.command.parameter.ExtractorMap;
import me.saiintbrisson.minecraft.command.parameter.interfaces.TypeAdapter;

/**
 * The CommandFrame is the core of the framework,
 * it registers the commands, adapters {@link AdapterMap}.
 *
 * @author Luiz Carlos Carvalho
 */
public interface CommandFrame<P> {
    /**
     * Get the plugin that owns this frame instance.
     *
     * @return the plugin that owns this frame instance.
     */
    P getPlugin();

    /**
     * Get the adapter map used by this frame instance
     * and its commands.
     *
     * @return the adapter map.
     */
    AdapterMap getAdapterMap();

    /**
     * Get the extract map used by this frame instance
     * and its commands.
     *
     * @return the extractor map.
     */
    ExtractorMap getExtractorMap();

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
     * @param instances the classes to be searched for.
     */
    void registerAll(Object... instances);

    /**
     * Registers a single command with the CommandInfo and Executor
     *
     * @param commandInfo the command metadata.
     * @param executor    the command to be executed.
     */
    <S> void registerCommand(CommandInfo commandInfo, CommandHandler<S> executor);

    /**
     * Registers a single completer for the given command.
     *
     * @param name     the command name.
     * @param executor the completer to be executed.
     */
    <S> void registerCompleter(String name, CompleterHandler<S> executor);

    /**
     * Unregisters a command.
     *
     * @param name the command to delete.
     * @return whether the command existed or not.
     */
    boolean unregisterCommand(String name);
}
