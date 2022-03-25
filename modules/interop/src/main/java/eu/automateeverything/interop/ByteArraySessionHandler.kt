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

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

class ByteArraySessionHandler(
    private val methodHandlers: List<MethodHandler>,
    private val binaryFormat: BinaryFormat) : SessionHandler<ByteArray, ByteArray> {

    override fun handleRequest(input: ByteArray, subscriptions: MutableList<SubscriptionHandler>): ByteArray {
        val requests = binaryFormat.decodeFromByteArray<List<JsonRpc2Request>>(input)
        val processed = requests.map { handleRequestInternal(it, subscriptions) }

        return binaryFormat.encodeToByteArray(processed)
    }

    override fun handleSubscriptions(subscriptions: MutableList<SubscriptionHandler>): ByteArray {
        val processed = subscriptions.flatMap {  it.collect() }

        return binaryFormat.encodeToByteArray(processed)
    }

    private fun handleRequestInternal(input: JsonRpc2Request, subscriptions: MutableList<SubscriptionHandler>): JsonRpc2Response {
        val handler = methodHandlers.firstOrNull { it.matches(input.method) }
        return if (handler != null) {
            val data = handler.handle(binaryFormat, input.params, subscriptions)
            JsonRpc2Response(id = input.id!!, result = data)
        } else {
            val error = JsonRpc2Error(404, "Method handler not found!")
            JsonRpc2Response(id = input.id!!, error = error)
        }
    }
}