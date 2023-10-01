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

package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.domain.events.PortUpdateType
import java.io.IOException
import java.math.BigDecimal

data class PortCapabilities(val canRead: Boolean, val canWrite: Boolean)

abstract class Port<V : PortValue>(
    private val factoryId: String,
    private val adapterId: String,
    val portId: String,
    private val eventBus: EventBus,
    val valueClazz: Class<V>,
    val capabilities: PortCapabilities,
    var sleepInterval: Long,
) {
    var lastSeenTimestamp: Long = 0L
        private set

    fun updateLastSeenTimeStamp(timeStamp: Long) {
        lastSeenTimestamp = timeStamp
        eventBus.broadcastPortUpdateEvent(factoryId, adapterId, PortUpdateType.LastSeenChange, this)
    }

    fun notifyValueUpdate() {
        eventBus.broadcastPortUpdateEvent(factoryId, adapterId, PortUpdateType.ValueChange, this)
    }

    open fun read(): V {
        if (capabilities.canRead) {
            return readInternal()
        } else {
            throw IOException("This port cannot read")
        }
    }

    abstract fun readInternal(): V

    @Suppress("UNCHECKED_CAST")
    fun write(value: BigDecimal) {
        val newValue = PortValueBuilder.buildFromDecimal(valueClazz, value) as V
        write(newValue)
    }

    open fun write(newValue: V) {
        if (capabilities.canWrite) {
            requestedValue = newValue
        } else {
            throw IOException("This port cannot write")
        }
    }

    var requestedValue: V? = null
        private set

    fun reset() {
        requestedValue = null
    }
}
