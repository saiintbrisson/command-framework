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

package me.saiintbrisson.bungee.command.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.saiintbrisson.bungee.command.target.BungeeTargetValidator;
import me.saiintbrisson.minecraft.command.CommandFrame;
import me.saiintbrisson.minecraft.command.command.CommandHolder;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exception.CommandException;
import me.saiintbrisson.minecraft.command.message.MessageType;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * @author Henry Fábio
 * Github: https://github.com/HenryFabio
 */
@Getter
@AllArgsConstructor
public class BungeeContext implements Context<CommandSender> {
    private final CommandSender sender;
    private final CommandTarget target;
    private final String[] args;

    private final CommandFrame<?, ?, ?> commandFrame;
    private final CommandHolder<?, ?> commandHolder;

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(new TextComponent(message));
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public boolean testPermission(String permission, boolean silent) throws CommandException {
        if (sender.hasPermission(permission)) {
            return true;
        }

        if (!silent) {
            throw new CommandException(MessageType.NO_PERMISSION, permission);
        }

        return false;
    }

    @Override
    public boolean testTarget(CommandTarget target, boolean silent) throws CommandException {
        if (BungeeTargetValidator.INSTANCE.validate(target, sender)) {
            return true;
        }

        if (!silent) {
            throw new CommandException(MessageType.INCORRECT_USAGE, target.name());
        }

        return false;
    }

    @Override
    public String getLabel() {
        throw new UnsupportedOperationException();
    }
}
