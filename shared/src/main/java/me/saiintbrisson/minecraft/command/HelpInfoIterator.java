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

package me.saiintbrisson.minecraft.command;

import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.command.Command;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * @author Luiz Carlos Carvalho
 */
@RequiredArgsConstructor
public class HelpInfoIterator implements Iterator<CommandInfo>, Iterable<CommandInfo> {
    private final Command root;

    private int index = -1;
    private HelpInfoIterator current;

    @Override
    public boolean hasNext() {
        return (current != null && current.hasNext()) || index < root.childCommandList().size();
    }

    @Override
    public CommandInfo next() {
        if (index == -1) {
            index++;
            return root.commandInfo();
        }

        if (current == null || !current.hasNext()) {
            current = new HelpInfoIterator(root.childCommandList().get(index));
            index++;
        }

        return current.next();
    }

    @NotNull
    @Override
    public Iterator<CommandInfo> iterator() {
        this.index = -1;
        this.current = null;
        return this;
    }
}
