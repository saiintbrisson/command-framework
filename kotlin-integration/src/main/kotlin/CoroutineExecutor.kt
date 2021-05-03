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

package me.saiintbrisson.commandframework

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import me.saiintbrisson.minecraft.command.CommandFrame
import me.saiintbrisson.minecraft.command.argument.eval.ArgumentEvaluator
import me.saiintbrisson.minecraft.command.command.CommandHolder
import me.saiintbrisson.minecraft.command.command.Context
import me.saiintbrisson.minecraft.command.exception.CommandException
import me.saiintbrisson.minecraft.command.executor.CommandExecutor
import me.saiintbrisson.minecraft.command.message.MessageHolder
import me.saiintbrisson.minecraft.command.message.MessageType
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.javaMethod

class CoroutineExecutor<S, C : CommandHolder<S, C>>(
    private val frame: CommandFrame<*, S, *>,
    scope: CoroutineScope,
    private val function: KFunction<Any>,
    private val message: MessageHolder,
    private val holder: CommandHolder<S, C>
) : CommandExecutor<S>, CoroutineScope {
    override val coroutineContext = scope.coroutineContext
    private val evaluator = run {
        ArgumentEvaluator<S>(frame.methodEvaluator.evaluateMethod(function.javaMethod))
    }

    override fun getEvaluator(): ArgumentEvaluator<S> = evaluator
    override fun getHolder(): CommandHolder<S, *> = holder

    override fun execute(context: Context<S>): Boolean {
        val coroutineContext = coroutineContext + CoroutineExceptionHandler { _, throwable ->
            when (throwable) {
                is InvocationTargetException -> {
                    val exception = throwable.targetException as? CommandException
                        ?: return@CoroutineExceptionHandler run {
                            throwable.targetException.printStackTrace()
                            context.sendMessage("§cAn internal error occurred, please contact the staff team.")
                        }

                    val message = when (val type = exception.messageType) {
                        null -> {
                            throwable.printStackTrace()
                            throwable.message?.let {
                                message.getReplacing(MessageType.ERROR, it)
                            }
                        }
                        else -> {
                            throwable.message?.let { message.getReplacing(type, it) }
                                ?: type.getDefault(holder)
                        }
                    }

                    context.sendMessage(message)
                }
                else -> {
                    throwable.printStackTrace()
                    context.sendMessage("§cAn internal error occurred, please contact the staff team.")
                }
            }
        }

        runBlocking(coroutineContext) {
            function.callSuspend(args = evaluator.parseArguments(context))
        }

        return false
    }
}
