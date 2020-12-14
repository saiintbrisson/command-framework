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

package me.saiintbrisson.minecraft.command;

import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandHolder;

import java.util.Iterator;

/**
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
 */

@RequiredArgsConstructor
public class CommandInfoIterator implements Iterator<CommandHolder<?, ?>> {
    private final CommandHolder<?, ?> root;

    private int index = -1;
    private CommandInfoIterator current;

    @Override
    public boolean hasNext() {
        return (current != null && current.hasNext()) || index < root.getChildCommandList().size();
    }

    @Override
    public CommandHolder<?, ?> next() {
        if (index == -1) {
            index++;
            return root;
        }

        if (current == null || !current.hasNext()) {
            current = new CommandInfoIterator(root.getChildCommandList().get(index));
            index++;
        }

        return current.next();
    }
}
