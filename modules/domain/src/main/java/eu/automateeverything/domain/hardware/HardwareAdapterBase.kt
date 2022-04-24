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

import eu.automateeverything.data.hardware.AdapterState
import eu.automateeverything.domain.events.EventBus
import java.util.*

abstract class HardwareAdapterBase<T : Port<*>>(
    protected val owningPluginId: String,
    override val id: String,
    protected val eventBus: EventBus
) : HardwareAdapter<T> {

    override var state = AdapterState.Initialized
    override var lastDiscoveryTime = 0L
    override var lastError: Throwable? = null
    override val ports: LinkedHashMap<String, T> = LinkedHashMap()
    private var hasNewPorts: Boolean = false

    override fun clearNewPortsFlag() {
        hasNewPorts = false
    }

    override fun hasNewPorts(): Boolean {
        return hasNewPorts
    }

    abstract suspend fun internalDiscovery(mode: DiscoveryMode)

    protected fun logDiscovery(message: String) {
        eventBus.broadcastDiscoveryEvent(owningPluginId, message)
    }

    protected fun broadcastPortUpdate(port: Port<*>) {
        eventBus.broadcastPortUpdateEvent(owningPluginId, id, port)
    }

    override suspend fun discover(mode: DiscoveryMode) {
        lastDiscoveryTime = Calendar.getInstance().timeInMillis
        state = AdapterState.Discovery

        internalDiscovery(mode)

        state = AdapterState.Operating
    }

    protected fun addPotentialNewPorts(newPorts: List<T>) {
        newPorts.forEach {
            if (!ports.containsKey(it.id)) {
                ports[it.id] = it
                broadcastPortUpdate(it)
                hasNewPorts = true
            }
        }
    }
}