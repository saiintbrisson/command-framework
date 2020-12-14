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
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
 */

@Getter
@AllArgsConstructor
public enum MessageType {
    ERROR("{error}",
      "§cAn error has been thrown: §f{error}§c.") {
        @Override
        public String getDefault(CommandHolder<?, ?> commandHolder) {
            return "";
        }
    },
    NO_PERMISSION("{permission}",
      "§cRequired permission: §f{permission}§c.") {
        @Override
        public String getDefault(CommandHolder<?, ?> commandHolder) {
            return commandHolder.getPermission();
        }
    },
    INCORRECT_USAGE("{usage}",
      "§cCorrect usage: §e/{usage}§c.") {
        @Override
        public String getDefault(CommandHolder<?, ?> commandHolder) {
            return commandHolder.getUsage();
        }
    },
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
