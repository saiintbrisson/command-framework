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

package me.saiintbrisson.minecraft.command.command;

import me.saiintbrisson.minecraft.command.HelpInfoIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The Command interface details information about a final
 * command, including instructions on how to execute and its
 * child commands.
 *
 * @author Luiz Carlos Carvalho
 */
public interface Command {
    /**
     * This command's metadata.
     *
     * @return the command info.
     */
    CommandInfo commandInfo();

    /**
     * The list of all child commands related to this command.
     *
     * @return list of child commands.
     */
    List<Command> childCommandList();

    /**
     * Get the Child command from this by the name, if it's not register it
     * will return null.
     *
     * @param name the child command name.
     * @return a child command.
     * @deprecated deprecated in favor of {@link Command#findChild(String, boolean)}.
     */
    @Nullable
    @Deprecated
    Command getChildCommand(String name);

    /**
     * Finds a child command, each dot represents another child.
     *
     * @param name    the name to search for.
     * @param recurse whether the algorithm should recurse through
     *                all its known children.
     * @return the child command if found, null otherwise.
     */
    @Nullable
    Command findChild(String name, boolean recurse);

    /**
     * Checks if the given string matches this command.
     *
     * @param name the string to be checked against.
     * @return whether the name matches.
     */
    default boolean matchesCommand(String name) {
        if (commandInfo().name().equalsIgnoreCase(name)) {
            return true;
        }

        for (String alias : commandInfo().aliases()) {
            if (alias.equalsIgnoreCase(name)) return true;
        }

        return false;
    }

    /**
     * The iterator returned by this method loops
     * through this command and all known child commands.
     *
     * @return an iterator.
     */
    @NotNull
    default Iterable<CommandInfo> helpInfoIterator() {
        return new HelpInfoIterator(this);
    }
}
