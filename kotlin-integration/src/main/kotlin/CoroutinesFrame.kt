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

package me.saiintbrisson.minecraft.command.kotlin

import kotlinx.coroutines.CoroutineScope
import me.saiintbrisson.minecraft.command.CommandFrame
import me.saiintbrisson.minecraft.command.annotation.Command
import me.saiintbrisson.minecraft.command.annotation.Completer
import me.saiintbrisson.minecraft.command.command.CommandHolder
import me.saiintbrisson.minecraft.command.command.CommandInfo
import me.saiintbrisson.minecraft.command.executor.CommandExecutor
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

/**
 * Coroutines command frame implementation, you can use
 * with any suspend function
 *
 * @param scope the target coroutine scope
 * @param delegate the remaining implementation of [CommandFrame]
 */
class CoroutinesFrame<P, S, C : CommandHolder<S, out C>>(
    val scope: CoroutineScope,
    private val delegate: CommandFrame<P, S, C>
) : CommandFrame<P, S, C> by delegate {
    override fun registerCommands(vararg objects: Any) {
        objects.forEach { commandHolder ->
            val functions = commandHolder::class.declaredMemberFunctions
                .filterIsInstance<KFunction<Any>>()

            functions.forEach register@{ function ->
                val command = function.findAnnotation<Command>() ?: return@register
                val holder = getCommand(command.name) ?: return@register

                registerCommand(
                    CommandInfo(command),
                    CoroutineExecutor(this, scope, function, messageHolder, holder)
                )
            }

            functions.forEach register@{ function ->
                val completer = function.findAnnotation<Completer>() ?: return@register

                registerCompleter(
                    completer.name,
                    CoroutineCompleter(commandHolder, function)
                )
            }
        }
    }

    override fun registerCommand(info: CommandInfo, commandExecutor: CommandExecutor<S>) {
        val evaluator = requireNotNull(commandExecutor.evaluator)

        requireNotNull(commandExecutor.holder).apply {
            setCommandName(info.name)
            setCommandDescription(info.description)
            setCommandUsage(info.usage.ifBlank { evaluator.buildUsage(info.name) })
            setCommandDescription(info.description)

            if (info.permission.isNotBlank()) {
                setCommandPermission(info.permission)
            }

            commandInfo = info
            aliasesList = info.aliases.toList()
        }

        delegate.registerCommand(info, commandExecutor)
    }
}
