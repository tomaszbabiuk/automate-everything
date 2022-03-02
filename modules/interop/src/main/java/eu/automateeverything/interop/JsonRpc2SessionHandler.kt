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

package eu.automateeverything.interop

import eu.automateeverything.data.Repository
import eu.automateeverything.data.icons.IconDto
import eu.automateeverything.data.inbox.InboxItemDto
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.tags.TagDto
import eu.automateeverything.interop.handlers.IconsHandler
import eu.automateeverything.interop.handlers.InstancesHandler
import eu.automateeverything.interop.handlers.MessagesHandler
import eu.automateeverything.interop.handlers.TagsHandler
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

class JsonRpc2SessionHandler(repository: Repository, private val format: BinaryFormat) : SessionHandler<JsonRpc2Request, JsonRpc2Response> {

    private val methodHandlers = listOf(
        InstancesHandler(repository),
        MessagesHandler(repository),
        IconsHandler(repository),
        TagsHandler(repository)
    )

    override fun handleRequest(input: JsonRpc2Request): JsonRpc2Response {
        val handler = methodHandlers.firstOrNull { it.matches(input.method) }
        return if (handler != null) {
            val data = handler.handle(format, input.params)
            JsonRpc2Response(id = input.id!!, result = data)
        } else {
            val error = JsonRpc2Error(404, "Method handler not found!")
            JsonRpc2Response(id = input.id!!, error = error)
        }
    }

    interface MethodHandler {
        fun matches(method: String): Boolean
        fun handle(format: BinaryFormat, params: ByteArray?) : ByteArray
    }






}