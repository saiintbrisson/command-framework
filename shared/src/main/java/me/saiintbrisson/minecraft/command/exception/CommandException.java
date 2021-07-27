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

package me.saiintbrisson.minecraft.command.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saiintbrisson.minecraft.command.message.MessageType;

/**
 * The CommandException is the default exception thrown
 * by the framework if any errors are thrown during the
 * execution of a command.
 *
 * @author Luiz Carlos Mourão
 */
@NoArgsConstructor
public class CommandException extends RuntimeException {

    @Getter
    private MessageType messageType;

    public CommandException(final MessageType messageType, final String message) {
        super(message);
        this.messageType = messageType;
    }

    public CommandException(final Throwable cause) {
        super(cause);
    }

    public CommandException(final String message) {
        super(message);
    }

}
