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

package me.saiintbrisson.bukkit.command.command;

import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.command.CommandHolder;

import java.util.Optional;

/**
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
 */
public class BukkitChildCommand extends BukkitCommand {
    private final BukkitCommand parentCommand;

    public BukkitChildCommand(BukkitFrame frame, String name, BukkitCommand parentCommand) {
        super(frame, name, parentCommand.getPosition() + 1);

        this.parentCommand = parentCommand;
    }

    @Override
    public String getFancyName() {
        return parentCommand.getFancyName() + " " + getName();
    }

    public Optional<CommandHolder<?, ?>> getParentCommand() {
        return Optional.of(parentCommand);
    }
}
