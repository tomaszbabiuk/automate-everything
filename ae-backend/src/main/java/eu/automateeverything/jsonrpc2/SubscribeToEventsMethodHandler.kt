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

import eu.automateeverything.interop.MethodHandler
import eu.automateeverything.interop.SubscriptionHandler
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import java.util.*

class SubscribeToEventsMethodHandler(private val subscriptionBuilder: EventsSubscriptionBuilder) : MethodHandler {
        override fun matches(method: String): Boolean {
            return method == "Subscribe"
        }

        override fun handle(format: BinaryFormat, params: ByteArray?, subscriptions: MutableList<SubscriptionHandler>): ByteArray {
            if (params != null) {
                val entityFilter: String = format.decodeFromByteArray(params)
                val id = "${Calendar.getInstance().timeInMillis}/$entityFilter"
                val subscription = subscriptionBuilder.build(id, entityFilter)
                subscriptions.add(subscription)
                return format.encodeToByteArray(id)
            } else {
                throw JsonRpc2Exception("Filter not defined. Pass the list of entities as a parameter to this request")
            }
        }
}