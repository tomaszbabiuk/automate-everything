/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package eu.automateeverything.interop

import eu.automateeverything.data.Repository
import eu.automateeverything.data.inbox.InboxItemDto
import eu.automateeverything.data.instances.InstanceDto
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToByteArray

class AESessionHandler(repository: Repository) : AccessSessionHandler {

    private val methodHandlers = listOf(
        InstancesHandler(repository),
        MessagesHandler(repository)
    )

    override fun handleRequest(request: JsonRpc2Request): JsonRpc2Response {
        val handler = methodHandlers.firstOrNull { it.matches(request.method) }
        return if (handler != null) {
            val data = handler.handle(request.id!!)
            JsonRpc2Response(id = request.id, result = data)
        } else {
            val error = JsonRpc2Error(404, "Method handler not found!")
            JsonRpc2Response(id = request.id!!, error = error)
        }
    }

    interface MethodHandler {
        fun matches(method: String): Boolean
        fun handle(id: String): ByteArray
    }

    class InstancesHandler(val repository: Repository) : MethodHandler {
        override fun matches(method: String): Boolean {
            return method == InstanceDto::class.java.simpleName
        }

        @OptIn(ExperimentalSerializationApi::class)
        override fun handle(id: String): ByteArray {
            val result = repository.getAllInstances()
            return Cbor.encodeToByteArray(result)
        }
    }

    class MessagesHandler(val repository: Repository) : MethodHandler {
        override fun matches(method: String): Boolean {
            return method == InboxItemDto::class.java.simpleName
        }

        @OptIn(ExperimentalSerializationApi::class)
        override fun handle(id: String): ByteArray {
            val result = repository.getInboxItems(100, 0)
            return Cbor.encodeToByteArray(result)
        }
    }
}