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
 * @author Luiz Carlos Mourão
 */
public enum CommandTarget {
    /**
     * The ALL target can be all senders listed below,
     * that means that it can receive command from
     * PLAYER and CONSOLE.
     */
    ALL,
    /**
     * The PLAYER target only accepts Players
     * to execute that command
     */
    PLAYER,
    /**
     * The CONSOLE target only accept the Console
     * to execute that command
     */
    CONSOLE
}
