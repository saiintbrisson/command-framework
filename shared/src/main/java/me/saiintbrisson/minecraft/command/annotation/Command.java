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

import me.saiintbrisson.minecraft.command.SenderType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Luiz Carlos Carvalho
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Command {
    /**
     * Defines the command path.
     * Input params are prefixed with a colon (':'):
     * `money|balance :username pay :amount`
     *
     * @return the command path.
     */
    String value();

    /**
     * @return the command aliases.
     */
    @Deprecated
    String[] aliases() default {};

    /**
     * @return the command description.
     */
    String description() default "";

    /**
     * @return the command usage example.
     */
    String usage() default "";

    /**
     * @return the required permission to execute the command.
     */
    String permission() default "";

    /**
     * @return the command target.
     */
    SenderType target() default SenderType.ANY;

    /**
     * Defines whether the command should be run in an async executor.
     * Some implementations might ignore this option as they are async by
     * default. This option requires an executor.
     *
     * @return whether the command should be run asynchronously.
     */
    boolean async() default false;
}
