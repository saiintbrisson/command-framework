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

package me.saiintbrisson.minecraft.command.annotation;

import me.saiintbrisson.minecraft.command.target.CommandTarget;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    /**
     * Defines the command name, sub-commands are split with dots
     * <p><p>
     * <b>Example:</b><p>
     * parentcommand<p>
     * parentcommand.subcommand<p>
     *
     * @return the command name
     */
    String name();

    /**
     * @return the command aliases
     */
    String[] aliases() default {};

    /**
     * @return the command description
     */
    String description() default "";

    /**
     * @return the command usage example
     */
    String usage() default "";

    /**
     * @return the required permission to execute the command
     */
    String permission() default "";

    /**
     * @return the command target
     */
    CommandTarget target() default CommandTarget.ALL;

    /**
     * Tells the executor how to run the command,
     * some implementations might ignore this option as they are async by default.
     * This option requires an executor.
     * @return whether the command should be ran asynchronously
     */
    boolean async() default false;
}
