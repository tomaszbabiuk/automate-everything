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

import eu.automateeverything.data.DataRepository
import eu.automateeverything.data.hardware.AdapterState
import eu.automateeverything.data.hardware.PortDto
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.domain.R
import eu.automateeverything.domain.WithStartStopScope
import eu.automateeverything.domain.automation.PortNotFoundException
import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.domain.events.PortUpdateType
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import eu.automateeverything.domain.inbox.Inbox
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.pf4j.PluginState
import org.pf4j.PluginStateEvent
import org.pf4j.PluginStateListener

class HardwareManager(
    pluginsCoordinator: PluginsCoordinator,
    private val eventBus: EventBus,
    private val inbox: Inbox,
    private val dataRepository: DataRepository,
) : WithStartStopScope<Void?>(), PluginStateListener, PortFinder {

    private val factories: MutableMap<HardwarePlugin, List<AdapterBundle>> = HashMap()

    init {
        pluginsCoordinator.addPluginStateListener(this)
    }

    /**
     * This method is called after every automation loop. The method is called in a blocking way.
     * The code from here must be executed before new automation loop.
     */
    fun afterAutomationLoop() {
        bundles().forEach { bundle -> bundle.adapter.executePendingChanges() }
    }

    private suspend fun cancelDiscoveryAndStopAdapters(factory: HardwarePlugin) {
        factories
            .filter { factory.pluginId == it.key.pluginId }
            .flatMap { it.value }
            .forEach {
                it.discoveryJob?.cancelAndJoin()
                it.adapter.stop()
            }
    }

    private suspend fun startAdaptersAndDiscover(factory: HardwarePlugin) = coroutineScope {
        factories
            .filter { factory.pluginId == it.key.pluginId }
            .flatMap { it.value }
            .forEach { bundle ->
                bundle.adapter.start()
                discover(bundle, DiscoveryMode.Startup)
            }
    }

    private suspend fun discover(bundle: AdapterBundle, mode: DiscoveryMode) = coroutineScope {
        if (bundle.discoveryJob != null && bundle.discoveryJob!!.isActive) {
            eventBus.broadcastDiscoveryEvent(
                bundle.owningPluginId,
                "Previous discovery is still pending, try again later"
            )
        } else {
            bundle.discoveryJob = async {
                bundle.adapter.discover(mode)
                bundle.adapter.ports.values.forEach {
                    val portSnapshot =
                        PortDto(
                            it.portId,
                            bundle.owningPluginId,
                            bundle.adapter.adapterId,
                            null,
                            null,
                            it.valueClazz.name,
                            it.capabilities.canRead,
                            it.capabilities.canWrite,
                            it.maxSleepInterval,
                            it.lastSeenTimestamp
                        )
                    dataRepository.updatePort(portSnapshot)
                    eventBus.broadcastPortUpdateEvent(
                        bundle.owningPluginId,
                        bundle.adapter.adapterId,
                        PortUpdateType.LastSeenChange,
                        it
                    )

                    val portNotReported = dataRepository.getPortById(it.portId) == null
                    if (portNotReported) {
                        inbox.sendMessage(
                            R.inbox_message_new_port_found_subject,
                            R.inbox_message_port_found_body(it.portId)
                        )
                    }
                }
            }
        }
    }

    private suspend fun removeFactory(factory: HardwarePlugin) {
        cancelDiscoveryAndStopAdapters(factory)
        factories.remove(factory)
    }

    private suspend fun addFactory(factory: HardwarePlugin) {
        if (factories.containsKey(factory)) {
            cancelDiscoveryAndStopAdapters(factory)
        }
        removeFactory(factory)

        val adaptersInFactory = factory.createAdapters()
        val adapterBundles =
            adaptersInFactory.map { adapter -> AdapterBundle(factory.pluginId, adapter) }.toList()
        factories[factory] = adapterBundles

        startAdaptersAndDiscover(factory)
    }

    override fun pluginStateChanged(event: PluginStateEvent) {
        val isHardwarePluginAffected = event.plugin.plugin is HardwarePlugin
        if (isHardwarePluginAffected) {
            val hardwarePlugin = event.plugin.plugin as HardwarePlugin
            if (event.pluginState == PluginState.STARTED) {
                startStopScope.launch { addFactory(hardwarePlugin) }
            }

            if (event.pluginState == PluginState.STOPPED) {
                startStopScope.launch { removeFactory(hardwarePlugin) }
            }
        }
    }

    fun bundles(): List<AdapterBundle> {
        return factories.values.flatten()
    }

    fun findPort(id: String): Pair<Port<*>, AdapterBundle>? {
        bundles().forEach { bundle ->
            if (bundle.adapter.ports.containsKey(id)) {
                val port = bundle.adapter.ports[id]!!
                return Pair(port, bundle)
            }
        }

        return null
    }

    fun executeAllPendingChanges() {
        bundles().forEach { it.adapter.executePendingChanges() }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : PortValue> listAllOfAnyType(valueClass: Class<T>): List<Port<T>> {
        return bundles()
            .flatMap { it.adapter.ports.values }
            .filter { it.valueClazz == valueClass }
            .map { it as Port<T> }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : PortValue> listAllOfInputType(valueClass: Class<T>): List<Port<T>> {
        return bundles()
            .flatMap { it.adapter.ports.values }
            .filter { it.valueClazz == valueClass && it.capabilities.canRead }
            .map { it as Port<T> }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : PortValue> listAllOfOutputType(valueClass: Class<T>): List<Port<T>> {
        return bundles()
            .flatMap { it.adapter.ports.values }
            .filter { it.valueClazz == valueClass && it.capabilities.canWrite }
            .map { it as Port<T> }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : PortValue> searchForAnyPort(valueClazz: Class<T>, id: String): Port<T> {
        val port =
            factories
                .flatMap { it.value }
                .flatMap { it.adapter.ports.values }
                .find { it.portId == id }

        if (port != null && port.valueClazz == valueClazz) {
            return port as Port<T>
        }

        throw PortNotFoundException(id)
    }

    override fun <T : PortValue> searchForInputPort(valueClazz: Class<T>, id: String): Port<T> {
        val anyPort = searchForAnyPort(valueClazz, id)
        if (anyPort.capabilities.canRead) {
            return anyPort
        }

        throw PortNotFoundException(id)
    }

    override fun <T : PortValue> searchForOutputPort(valueClazz: Class<T>, id: String): Port<T> {
        val anyPort = searchForAnyPort(valueClazz, id)
        if (anyPort.capabilities.canWrite) {
            return anyPort
        }

        throw PortNotFoundException(id)
    }

    override fun checkNewPorts(): Boolean {
        var result = false
        bundles().forEach { bundle ->
            val hasNewPorts = bundle.adapter.hasNewPorts()
            if (hasNewPorts) {
                result = true
            }
        }

        return result
    }

    override fun clearNewPortsFlag() {
        bundles().forEach { it.adapter.clearNewPortsFlag() }
    }

    fun scheduleDiscovery(factoryId: String) {
        factories
            .filter { it.key.pluginId == factoryId }
            .flatMap { it.value }
            .forEach { startStopScope.launch { discover(it, DiscoveryMode.Manual) } }
    }

    fun countNonOperatingAdapters(): Int {
        return bundles().filter { it.adapter.state != AdapterState.Operating }.size
    }
}
