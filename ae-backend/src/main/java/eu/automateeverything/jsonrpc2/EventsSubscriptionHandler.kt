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

import eu.automateeverything.domain.events.EventsBus
import eu.automateeverything.domain.events.LiveEvent
import eu.automateeverything.domain.events.LiveEventsListener
import eu.automateeverything.interop.JsonRpc2Response
import eu.automateeverything.interop.SubscriptionHandler
import eu.automateeverything.mappers.LiveEventsMapper
import kotlinx.serialization.BinaryFormat
import java.util.concurrent.ConcurrentLinkedQueue

class EventsSubscriptionHandler(
    private val id: String,
    private val eventsBus: EventsBus,
    private val eventsMapper: LiveEventsMapper,
    private val binaryFormat: BinaryFormat,
    private val entityFilter: String
) : SubscriptionHandler,
    LiveEventsListener {

    private val queue = ConcurrentLinkedQueue<Pair<Any, (BinaryFormat) -> ByteArray>>()

    init {
        eventsBus.subscribeToGlobalEvents(this)
    }

    protected fun finalize() {
        eventsBus.unsubscribeFromGlobalEvents(this)
    }

    override fun collect(): List<JsonRpc2Response> {
        return queue.associateBy(keySelector = {
                it.first.javaClass.simpleName
        }, valueTransform = {
            it.second.invoke(binaryFormat)
        }).map {
            JsonRpc2Response(id = id, result = it.value)
        }
    }

    override fun reset() {
        queue.clear()
    }

    override fun onEvent(event: LiveEvent<*>) {
        eventsMapper
            .map(event)
            .filter { it.first::class.java.simpleName == entityFilter }
            .forEach { queue.offer(it) }
    }
}