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

package me.saiintbrisson.minecraft.command.command;

import me.saiintbrisson.minecraft.command.CommandFrame;
import me.saiintbrisson.minecraft.command.argument.TypeAdapter;
import me.saiintbrisson.minecraft.command.exception.CommandException;
import me.saiintbrisson.minecraft.command.target.CommandTarget;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
 */
public interface Context<S> {
    /**
     * @return the label used to execute the command
     */
    String getLabel();

    /**
     * @return who sent the command
     */
    S getSender();

    /**
     * @return the executor type
     */
    CommandTarget getTarget();

    /**
     * @return the arguments array
     */
    String[] getArgs();


    /**
     * @return the number of arguments
     */
    default int argsCount() {
        return getArgs().length;
    }

    /**
     * @param index the index of the argument
     * @return the argument - null if the index is out of bounds
     */
    default String getArg(int index) {
        try {
            return getArgs()[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    default <T> T getArg(int index, Class<T> type) {
        return (T) getCommandFrame().getAdapterMap().get(type).convertNonNull(getArg(index));
    }

    /**
     * Gets all args between indexes from and to
     *
     * @param from defines the start of the array relative to the arguments, inclusive
     * @param to   defines the end of the array relative to the arguments, exclusive
     * @return the arguments array - null if the indexes are out of bounds
     */
    default String[] getArgs(int from, int to) {
        try {
            return Arrays.copyOfRange(getArgs(), from, to);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    default <T> T[] getArgs(int from, int to, Class<T> type) {
        try {
            final TypeAdapter<?> adapter = getCommandFrame().getAdapterMap().get(type);
            final T[] instance = (T[]) Array.newInstance(type,  to - from);

            for (int i = from; i <= to; i++) {
                instance[i - from] = (T) adapter.convertNonNull(getArg(i));
            }

            return instance;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }


    /**
     * Sends a message to the executor
     *
     * @param message the message to be sent
     */
    void sendMessage(String message);

    /**
     * Sends multiple messages to the executor
     *
     * @param messages the messages to be sent
     */
    void sendMessage(String[] messages);

    /**
     * Sends a message formatting it with the String#format() method
     *
     * @param message the message to be sent
     * @param objects the objects to be inserted
     */
    default void sendMessage(String message, Object... objects) {
        sendMessage(String.format(message, objects));
    }


    /**
     * Tests whether the executor has a permission
     *
     * @param permission the permission to be tested
     * @param silent     whether a exception should be thrown
     * @return the test result if silent
     */
    boolean testPermission(String permission, boolean silent) throws CommandException;

    /**
     * Tests whether the executor is a target
     *
     * @param target the target to be tested
     * @param silent whether a exception should be thrown
     * @return the test result if silent
     */
    boolean testTarget(CommandTarget target, boolean silent) throws CommandException;

    /**
     * @return this command's frame
     */
    CommandFrame<?, ?, ?> getCommandFrame();

    /**
     * @return this command's holder
     */
    CommandHolder<?, ?> getCommandHolder();
}
