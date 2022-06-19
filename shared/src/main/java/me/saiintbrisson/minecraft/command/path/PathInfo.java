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

package me.saiintbrisson.minecraft.command.path;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import me.saiintbrisson.minecraft.command.SenderType;
import me.saiintbrisson.minecraft.command.annotation.Command;
import org.jetbrains.annotations.NotNull;

/**
 * @author Luiz Carlos Carvalho
 */
@Getter
@Builder
@AllArgsConstructor
@Accessors(fluent = true)
public final class PathInfo {
    /**
     * Defines the command path.
     * Can also be thought of as the command's name.
     * Example: `money :username pay :value`, where
     * strings beginning with colons represent input tokens.
     */
    @NotNull
    private final String path;

    /**
     * Defines the description of the command.
     * Defaults to an empty string.
     */
    @NotNull
    @Builder.Default
    private final String description = "";

    /**
     * Defines the command usage.
     * Defaults to an auto generated usage message.
     */
    @NotNull
    @Builder.Default
    private final String usage = "";

    /**
     * Defines the permission required to execute
     * the command.
     * Defaults to an empty string.
     */
    @NotNull
    @Builder.Default
    private final String permission = "";

    /**
     * Defines the expected sender type to execute the command.
     * Defaults to any sender.
     */
    @NotNull
    @Builder.Default
    private final SenderType target = SenderType.ANY;

    /**
     * Tells the executor how to run the command,
     * some implementations might ignore this option as they are async by default.
     * This option requires an executor.
     */
    @Builder.Default
    private final boolean async = false;

    public PathInfo(@NotNull String path) {
        this(
          path,
          "",
          "",
          "",
          SenderType.ANY,
          false
        );
    }

    public static PathInfo combine(PathInfo node, PathInfo leaf) {
        return new PathInfo(
          String.format("%s %s", node.path, leaf.path),
          leaf.description,
          leaf.usage,
          leaf.permission,
          leaf.target,
          leaf.async
        );
    }

    public static PathInfo ofCommand(Command command) {
        return new PathInfo(
          command.value(),
          command.description(),
          command.usage(),
          command.permission(),
          command.target(),
          command.async()
        );
    }

    @Override
    public String toString() {
        return "PathInfo{" +
          "path='" + path + '\'' +
          ", description='" + description + '\'' +
          ", usage='" + usage + '\'' +
          ", permission='" + permission + '\'' +
          ", target=" + target +
          ", async=" + async +
          '}';
    }
}
