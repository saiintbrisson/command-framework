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
import me.saiintbrisson.minecraft.command.SenderType;

/**
 * @author Luiz Carlos Carvalho
 */
@Getter
@Builder
@AllArgsConstructor
@Accessors(fluent = true)
public final class CommandInfo {
    /**
     * Defines the command path.
     * Can also be thought of as the command's name.
     * Example: `money :username pay :value`, where
     * strings beginning with colons represent input tokens.
     */
    @NonNull
    private final String path;

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
    private final SenderType target = SenderType.ANY;
    /**
     * Tells the executor how to run the command,
     * some implementations might ignore this option as they are async by default.
     * This option requires an executor.
     */
    @Builder.Default
    private final boolean async = false;

    public static CommandInfo ofCommand(Command command) {
        return new CommandInfo(
          command.value(),
          command.description(),
          command.usage(),
          command.permission(),
          command.target(),
          command.async()
        );
    }
}
