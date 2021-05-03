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

import kotlinx.coroutines.runBlocking
import me.saiintbrisson.minecraft.command.command.Context
import me.saiintbrisson.minecraft.command.executor.CompleterExecutor
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

@OptIn(ExperimentalStdlibApi::class)
@Suppress("UNCHECKED_CAST")
class CoroutineCompleter<S>(
    private val holder: Any,
    function: KFunction<Any>
) : CompleterExecutor<S> {
    private val function: KFunction<Iterable<Any>>
    private val parameter = function.parameters.firstOrNull()

    init {
        require(function.returnType.isSubtypeOf(typeOf<Iterable<Any>>())) {
            "A command completer can only return an Iterable instance"
        }

        require(function.parameters.size <= 1) {
            "A command completer can have only one parameter"
        }

        require(parameter?.type?.isSubtypeOf(typeOf<Context<S>>()) ?: true) {
            "A command completer can have only a Context parameter"
        }

        this.function = function as KFunction<Iterable<Any>>
    }

    override fun execute(context: Context<S>): List<String> = runBlocking {
        function.callSuspend(holder, context).map(Any::toString)
    }
}
