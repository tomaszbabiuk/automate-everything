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

package eu.automateeverything.jsonrpc2

import eu.automateeverything.data.DataRepository
import eu.automateeverything.interop.MethodHandler
import eu.automateeverything.interop.SubscriptionHandler
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.encodeToByteArray

class MessagesMethodHandler(val dataRepository: DataRepository) : MethodHandler {
    override fun matches(method: String): Boolean {
        return method == "GetMessages"
    }

    override fun handle(
        format: BinaryFormat,
        params: ByteArray?,
        subscriptions: MutableList<SubscriptionHandler>
    ): ByteArray {
        val result = dataRepository.getInboxItems(100, 0)
        return format.encodeToByteArray(result)
    }
}
