/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
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

package eu.automateeverything.domain.events

import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList

class NumberedEventsSink : EventsSink {

    var eventCounter = 0

    private val listeners = Collections.synchronizedList(ArrayList<LiveEventsListener>())

    val events = ArrayList<LiveEvent<*>>()
    val messages = ArrayList<LiveEvent<*>>()

    override fun addAdapterEventListener(listener: LiveEventsListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: LiveEventsListener) {
        listeners.remove(listener)
    }

    private fun enqueue(payload: LiveEventData, target: ArrayList<LiveEvent<*>>, maxSize: Int) {
        if (target.size > maxSize) {
            target.removeAt(0)
        }

        println(payload)

        eventCounter++
        val now = Calendar.getInstance().timeInMillis
        val event = LiveEvent(now, eventCounter, payload.javaClass.simpleName, payload)
        target.add(event)
        listeners.filterNotNull().forEach { listener -> listener.onEvent(event) }
    }

    override fun  broadcastEvent(payload: LiveEventData) {
        enqueue(payload, events, MAX_EVENTS_SIZE)
    }

    override fun broadcastMessage(payload: LiveEventData) {
        enqueue(payload, messages, MAX_INBOX_SIZE)
    }

    override fun reset() {
        eventCounter = 0
        events.clear()
    }

    override fun removeRange(filter: Predicate<in LiveEvent<*>>) {
        events.removeIf(filter)
    }

    override fun all(): List<LiveEvent<*>> {
        return events
    }

    companion object {
        const val MAX_EVENTS_SIZE = 200
        const val MAX_INBOX_SIZE = 100
    }
}