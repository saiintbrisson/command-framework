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

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
 */
public interface CommandHolder<S, C extends CommandHolder<S, C>> extends Iterable<CommandHolder<?, ?>> {
    int getPosition();

    CommandExecutor<S> getCommandExecutor();
    CompleterExecutor<S> getCompleterExecutor();

    default Optional<CommandHolder<?, ?>> getParentCommand() {
        return Optional.empty();
    }
    List<C> getChildCommandList();
    C getChildCommand(String name);

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
