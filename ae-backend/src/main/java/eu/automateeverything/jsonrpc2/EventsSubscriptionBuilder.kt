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

import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.mappers.LiveEventsMapper
import kotlinx.serialization.BinaryFormat

class EventsSubscriptionBuilder(private val eventBus: EventBus,
                                private val eventsMapper: LiveEventsMapper,
                                private val binaryFormat: BinaryFormat
) {

    fun build(id: String, entityFilter: String): EventsSubscriptionHandler {
        return EventsSubscriptionHandler(id, eventBus, eventsMapper, binaryFormat, entityFilter)
    }
}