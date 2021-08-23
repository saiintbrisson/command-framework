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

package me.saiintbrisson.minecraft.command.executor;

import me.saiintbrisson.minecraft.command.argument.eval.ArgumentEvaluator;
import me.saiintbrisson.minecraft.command.command.CommandHolder;
import me.saiintbrisson.minecraft.command.command.Context;

/**
 * The BukkitCommandExecutor is the main executor of each
 * method that is listed as a Command, it invokes the method
 * and executes everything inside.
 *
 * @author Luiz Carlos Mourão
 */
@FunctionalInterface
public interface CommandExecutor<S> {

    /**
     * Executes the command with the provided context
     * <p>Returns false if the execution wasn't successful</p>
     * @param context Context
     *
     * @return boolean
     */
    boolean execute(Context<S> context);

    default ArgumentEvaluator<S> getEvaluator() {
        return null;
    }

    default CommandHolder<S, ?> getHolder() {
        return null;
    }
}
