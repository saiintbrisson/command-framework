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

package me.saiintbrisson.minecraft.command.command;

import me.saiintbrisson.minecraft.command.CommandInfoIterator;
import me.saiintbrisson.minecraft.command.executor.CommandExecutor;
import me.saiintbrisson.minecraft.command.executor.CompleterExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * A CommandHolder implements all the inner workings necessary to make
 * a command work, it contains the main information about
 * that command {@link CommandInfo} position and Parent/Child commands
 *
 * @author Luiz Carlos Mourão
 */
public interface CommandHolder<S, C extends CommandHolder<S, C>> extends Iterable<CommandHolder<?, ?>> {
    /**
     * The child position, where root is set to 0.
     * @return the child position.
     * @deprecated Deprecated as this wasn't needed.
     */
    @Deprecated
    int getPosition();

    CommandExecutor<S> getCommandExecutor();
    CompleterExecutor<S> getCompleterExecutor();

    default Optional<CommandHolder<?, ?>> getParentCommand() {
        return Optional.empty();
    }
    List<C> getChildCommandList();

    /**
     * Get the Child command from this by the name, if it's not register it
     * will return null.
     * @param name String
     *
     * @return BukkitChildCommand
     * @deprecated deprecated in favor of {@link CommandHolder#findChild(String, boolean)}.
     */
    @Nullable
    @Deprecated
    C getChildCommand(String name);

    /**
     * Finds a child command, each dot represents another child.
     * @param name the name to search for.
     * @param recurse if true, the algorithm will recurse through all its known children.
     *
     * @return BukkitChildCommand the child command if found, null otherwise.
     */
    @Nullable
    C findChild(String name, boolean recurse);

    CommandInfo getCommandInfo();

    String getName();
    String getFancyName();
    List<String> getAliasesList();

    String getPermission();
    String getUsage();
    String getDescription();

    default boolean equals(String name) {
        if (getName().equalsIgnoreCase(name)) {
            return true;
        }

        for (String alias : getAliasesList()) {
            if (alias.equalsIgnoreCase(name)) return true;
        }

        return false;
    }

    @NotNull
    @Override
    default Iterator<CommandHolder<?, ?>> iterator() {
        return new CommandInfoIterator(this);
    }
}
