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

import lombok.*;
import lombok.experimental.Accessors;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.target.CommandTarget;

/**
 * @author Luiz Carlos Carvalho
 */
@Getter
@Builder
@AllArgsConstructor
@Accessors(fluent = true)
public final class CommandInfo {
    /**
     * Defines the command name.
     * <p>One can define a sub-command by splitting
     * the name with dots: "foo.bar".
     */
    @NonNull
    private final String name;
    /**
     * Defines the array of aliases of the command,
     * if it doesn't have aliases it return a empty
     * array of strings
     */
    @NonNull
    @Builder.Default
    private final String[] aliases = new String[0];
    /**
     * Defines the description of the command,
     * if it wasn't provided, it returns a empty
     * String
     */
    @Builder.Default
    private final String description = "";
    /**
     * Defines the command usage for the MessageuHolder,
     * if it's empty, returns a empty String
     */
    @Builder.Default
    private final String usage = "";
    /**
     * Defines the permission required to execute
     * the command, if it's empty the default permission
     * is a empty String
     */
    @Builder.Default
    private final String permission = "";
    /**
     * Defines the CommandTarget of the command,
     * if it's empty, it returns a ALL target.
     */
    @NonNull
    @Builder.Default
    private final CommandTarget target = CommandTarget.ANY;
    /**
     * Tells the executor how to run the command,
     * some implementations might ignore this option as they are async by default.
     * This option requires an executor.
     */
    @Builder.Default
    private final boolean async = false;

    public CommandInfo(Command command) {
        this(
                command.value(),
                command.aliases(),
                command.description(),
                command.usage(),
                command.permission(),
                command.target(),
                command.async()
        );
    }
}
