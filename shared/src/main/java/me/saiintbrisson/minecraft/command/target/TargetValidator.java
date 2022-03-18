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

package me.saiintbrisson.minecraft.command.target;

/**
 * Responsible for validating if the given sender
 * is compatible with the defined {@link CommandTarget}.
 *
 * @author Luiz Carlos Carvalho
 */
public interface TargetValidator {
    /**
     * Tries to validate the sender against the given {@link CommandTarget}.
     *
     * @param target the target to test against.
     * @param sender the sender.
     * @return whether the sender is valid.
     */
    boolean validate(CommandTarget target, Object sender);

    /**
     * Returns the corresponding {@link CommandTarget} for the given sender.
     *
     * @param sender the sender to be converted.
     * @return the corresponding {@link CommandTarget}.
     */
    CommandTarget fromSender(Object sender);
}
