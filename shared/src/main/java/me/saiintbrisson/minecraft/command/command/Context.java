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

package me.saiintbrisson.minecraft.command.command;

import me.saiintbrisson.minecraft.command.exception.InsufficientPermissionsException;
import me.saiintbrisson.minecraft.command.exception.MismatchedTargetException;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Arrays;

/**
 * The context is where all information from the command dispatcher
 * is stored, such as the sender, arguments and label
 *
 * @author Luiz Carlos Carvalho
 */
public interface Context<S> {
    /**
     * The command executed.
     *
     * @return the command executed.
     */
    Command command();

    /**
     * The label used by the sender.
     *
     * @return the label used.
     */
    String label();

    /**
     * Who executed the command.
     *
     * @return the sender.
     */
    S sender();

    /**
     * Get the type of the sender.
     *
     * @return the sender type.
     */
    CommandTarget senderType();

    /**
     * Get all arguments passed to this command.
     *
     * @return the arguments.
     */
    String[] args();

    /**
     * Get the arguments count passed to this command.
     *
     * @return the number of arguments.
     */
    default int argsCount() {
        return args().length;
    }

    /**
     * Get the arg at the given index.
     *
     * @param index the index of the argument.
     * @return the argument, null if the index is out of bounds.
     */
    default String getArg(int index) {
        try {
            return args()[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Gets all args between indexes <code>from</code> and <code>to</code>.
     *
     * @param from defines the start of the array relative
     *             to the arguments, inclusive.
     * @param to   defines the end of the array relative
     *             to the arguments, exclusive.
     * @return the arguments array,
     * null if the indexes are out of bounds.
     */
    default String[] getArgs(int from, int to) {
        try {
            return Arrays.copyOfRange(args(), from, to);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Sends a message to the sender.
     *
     * @param message the message to be sent.
     */
    void send(String message);

    /**
     * Sends multiple messages to the sender.
     *
     * @param messages the messages to be sent.
     */
    void send(String[] messages);

    /**
     * Sends a message to the sender.
     *
     * @param component the component to be sent.
     */
    void send(BaseComponent component);

    /**
     * Sends a message to the sender.
     * In contrast to {@link Context#send(String[])}, this method
     * will only send one message, unless a line feed is
     * explicitly defined.
     *
     * @param components the components to be sent.
     */
    void send(BaseComponent[] components);

    /**
     * Tests whether the sender has the given permission.
     *
     * @param permission the permission to be tested.
     * @param silent     whether an exception should be thrown.
     * @return the test result if silent.
     */
    boolean checkPermission(String permission, boolean silent) throws InsufficientPermissionsException;

    /**
     * Tests whether the sender matches the given target.
     *
     * @param target the target to be tested against.
     * @param silent whether an exception should be thrown.
     * @return the test result if silent.
     */
    boolean checkTarget(CommandTarget target, boolean silent) throws MismatchedTargetException;
}
