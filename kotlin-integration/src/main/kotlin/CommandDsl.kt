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

import me.saiintbrisson.minecraft.command.command.CommandHolder
import me.saiintbrisson.minecraft.command.command.CommandInfo
import me.saiintbrisson.minecraft.command.command.Context
import me.saiintbrisson.minecraft.command.target.CommandTarget
import kotlin.reflect.jvm.ExperimentalReflectionOnLambdas
import kotlin.reflect.jvm.reflect

@OptIn(ExperimentalReflectionOnLambdas::class)
fun <P, S, C : CommandHolder<S, out C>> CoroutinesFrame<P, S, out C>.registerCommand(
    name: String,
    dsl: CommandDsl<S>.() -> Unit
) {
    val holder = CommandDsl<S>(name).apply(dsl)
    val info = holder.buildInfo()
    val function = requireNotNull(holder.handler.reflect()) {
        "Could not reflect command dsl"
    }
    val commandHolder = requireNotNull(getCommand(info.name)) {
        "Failed to find command holder for ${info.name}"
    }

    registerCommand(info, CoroutineExecutor(this, scope, function, messageHolder, commandHolder))
}

class CommandDsl<S>(var name: String) {
    var description = ""
    var usage = ""
    var target = CommandTarget.ALL
    var permission = ""
    var aliases = mutableListOf<String>()

    internal lateinit var handler: suspend (Context<S>) -> Unit

    fun handle(handler: suspend (Context<S>) -> Unit) {
        this.handler = handler
    }
}

private fun <S> CommandDsl<S>.buildInfo(): CommandInfo = CommandInfo(
    name,
    aliases.toTypedArray(),
    description,
    usage,
    permission,
    target,
    true
)
