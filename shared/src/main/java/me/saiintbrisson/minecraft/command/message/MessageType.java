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

package me.saiintbrisson.minecraft.command.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.saiintbrisson.minecraft.command.command.CommandHolder;

/**
 * The MessageType contains the default messages for the errors
 * as well that values can be edited.
 *
 * <p>Placeholders can be put on each of those errors</p>
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
 */
@Getter
@AllArgsConstructor
public enum MessageType {

    /**
     * Used when a error is thrown, the {error} can be
     * used to send the message of error
     */
    ERROR("{error}",
      "§cAn error has been thrown: §f{error}§c.") {
        @Override
        public String getDefault(CommandHolder<?, ?> commandHolder) {
            return "";
        }
    },
    /**
     * Used when a player doesn't have a permission,
     * the {permission} can be used to send the permission
     */
    NO_PERMISSION("{permission}",
      "§cRequired permission: §f{permission}§c.") {
        @Override
        public String getDefault(CommandHolder<?, ?> commandHolder) {
            return commandHolder.getPermission();
        }
    },
    /**
     * Used when a player doesn't use the command correctly,
     * the {usage} can be used to send the correct usage
     */
    INCORRECT_USAGE("{usage}",
      "§cCorrect usage: §e/{usage}§c.") {
        @Override
        public String getDefault(CommandHolder<?, ?> commandHolder) {
            return commandHolder.getUsage();
        }
    },
    /**
     * Used when a player doesn't use the target correctly,
     * the {target} can be used to send the correct target
     */
    INCORRECT_TARGET("{target}",
      "§cYou cannot execute this command. Targeted to: §f{target}§c.") {
        @Override
        public String getDefault(CommandHolder<?, ?> commandHolder) {
            return commandHolder.getCommandInfo().getTarget().name();
        }
    };

    private final String placeHolder;
    private final String defMessage;

    public abstract String getDefault(CommandHolder<?, ?> commandHolder);
}
