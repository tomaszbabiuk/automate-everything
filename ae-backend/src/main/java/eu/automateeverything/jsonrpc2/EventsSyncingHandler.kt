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

import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.events.LiveEvent
import eu.automateeverything.domain.events.LiveEventsListener
import eu.automateeverything.interop.JsonRpc2Response
import eu.automateeverything.interop.SyncingHandler
import eu.automateeverything.mappers.LiveEventsMapper
import kotlinx.serialization.BinaryFormat
import java.util.concurrent.ConcurrentLinkedQueue

class EventsSyncingHandler(
    private val eventsSink: EventsSink,
    private val eventsMapper: LiveEventsMapper,
    private val binaryFormat: BinaryFormat
) : SyncingHandler,
    LiveEventsListener {

    private val queue = ConcurrentLinkedQueue<Pair<Any, (BinaryFormat) -> ByteArray>>()

    init {
        eventsSink.addEventListener(this)
    }

    protected fun finalize() {
        eventsSink.removeListener(this)
    }

    override fun collect(): List<JsonRpc2Response> {
        return queue.associateBy(keySelector = {
                it.first.javaClass.simpleName
        }, valueTransform = {
            it.second.invoke(binaryFormat)
        }).map {
            JsonRpc2Response(subscription = it.key, id = it.key, result = it.value)
        }
    }

    override fun onEvent(event: LiveEvent<*>) {
        eventsMapper
            .map(event)
            .forEach { queue.offer(it) }
    }
}