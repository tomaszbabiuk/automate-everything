package eu.geekhome.domain.hardware

import eu.geekhome.domain.WithStartStopScope
import eu.geekhome.domain.automation.PortNotFoundException
import eu.geekhome.domain.extensibility.PluginsCoordinator
import eu.geekhome.data.Repository
import eu.geekhome.data.hardware.PortDto
import eu.geekhome.data.settings.SettingsDto
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.inbox.Inbox
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.pf4j.PluginState
import org.pf4j.PluginStateEvent
import org.pf4j.PluginStateListener

class HardwareManager(
    private val pluginsCoordinator: PluginsCoordinator,
    private val eventsSink: EventsSink,
    private val inbox: Inbox,
    private val repository: Repository,
) : WithStartStopScope(), PluginStateListener, PortFinder {

    private val factories: MutableMap<HardwareAdapterFactory, List<AdapterBundle>> = HashMap()

    init {
        pluginsCoordinator.addPluginStateListener(this)
    }

    /**
     * This method is called after every automation loop. The method is called in a blocking way. The code from here
     * must be executed before new automation loop.
     */
    fun afterAutomationLoop() {
        bundles()
            .forEach { bundle ->
                bundle.adapter.executePendingChanges()
            }
    }

    private suspend fun cancelDiscoveryAndStopAdapters(factory: HardwareAdapterFactory) {
        factories
            .filter { factory.owningPluginId == it.key.owningPluginId }
            .flatMap { it.value }
            .forEach {
                it.discoveryJob?.cancelAndJoin()
                it.adapter.stop()
            }
    }

    private suspend fun startAdaptersAndDiscover(factory: HardwareAdapterFactory) = coroutineScope {
        factories
            .filter { factory.owningPluginId == it.key.owningPluginId }
            .flatMap { it.value }
            .forEach { bundle ->
                bundle.adapter.start(eventsSink, extractPluginSettings(bundle.owningPluginId))
                discover(bundle)
            }
    }

    private suspend fun discover(bundle: AdapterBundle) = coroutineScope {
        if (bundle.discoveryJob != null && bundle.discoveryJob!!.isActive) {
            eventsSink.broadcastDiscoveryEvent(bundle.owningPluginId, "Previous discovery is still pending, try again later")
        } else {
            bundle.discoveryJob = async {
                bundle.adapter.discover(eventsSink)
                bundle.adapter.ports.values.forEach {
                    val portSnapshot = PortDto(it.id, bundle.owningPluginId, bundle.adapter.id,
                        null, null, it.valueClazz.name, it.canRead, it.canWrite, false)
                    repository.updatePort(portSnapshot)
                    eventsSink.broadcastPortUpdateEvent(bundle.owningPluginId, bundle.adapter.id, it)

                    val portNotReported = repository.getPortById(it.id) == null
                    if (portNotReported) {
                        inbox.sendNewPortDiscovered(it.id)
                    }
                }
            }
        }
    }

    private fun extractPluginSettings(pluginId: String): List<SettingsDto> {
        return pluginsCoordinator
            .getPluginSettingGroups(pluginId)
            .mapNotNull { repository.getSettingsByPluginIdAndClazz(pluginId, it.javaClass.name) }
    }

    private suspend fun removeFactory(factory: HardwareAdapterFactory) {
        cancelDiscoveryAndStopAdapters(factory)
        factories.remove(factory)
    }

    private suspend fun addFactory(factory: HardwareAdapterFactory) {
        if (factories.containsKey(factory)) {
            cancelDiscoveryAndStopAdapters(factory)
        }
        removeFactory(factory)

        val adaptersInFactory = factory.createAdapters()
        val adapterBundles = adaptersInFactory
            .map { adapter -> AdapterBundle(factory.owningPluginId, adapter) }
            .toList()
        factories[factory] = adapterBundles

        startAdaptersAndDiscover(factory)
    }

    override fun pluginStateChanged(event: PluginStateEvent) {
        val isHardwarePluginAffected = event.plugin.plugin is HardwarePlugin
        if (isHardwarePluginAffected) {
            val hardwarePlugin = event.plugin.plugin as HardwarePlugin
            if (event.pluginState == PluginState.STARTED) {
                startStopScope.launch {
                    addFactory(hardwarePlugin)
                }
            }

            if (event.pluginState == PluginState.STOPPED) {
                startStopScope.launch {
                    removeFactory(hardwarePlugin)
                }
            }
        }
    }

    fun bundles(): List<AdapterBundle> {
        return factories.values.flatten()
    }

    fun findPort(id: String): Pair<Port<*>, AdapterBundle>? {
        bundles().forEach { bundle ->
            if  (bundle.adapter.ports.containsKey(id)) {
                val port = bundle.adapter.ports[id]!!
                return Pair(port, bundle)
            }
        }

        return null
    }

    fun executeAllPendingChanges() {
        bundles()
            .forEach { it.adapter.executePendingChanges() }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : PortValue> searchForAnyPort(
        valueClazz: Class<T>,
        id: String
    ): Port<T> {
        val port = factories
            .flatMap { it.value }
            .flatMap { it.adapter.ports.values }
            .find { it.id == id }

        if (port != null && port.valueClazz == valueClazz) {
            return port as Port<T>
        }

        throw PortNotFoundException(id)
    }

    override fun <T : PortValue> searchForInputPort(valueClazz: Class<T>, id: String): InputPort<T> {
        val anyPort = searchForAnyPort(valueClazz, id)
        if (anyPort is InputPort<T>) {
            return anyPort
        }

        throw PortNotFoundException(id)
    }

    override fun <T : PortValue> searchForOutputPort(valueClazz: Class<T>, id: String): OutputPort<T> {
        val anyPort = searchForAnyPort(valueClazz, id)
        if (anyPort is OutputPort<T>) {
            return anyPort
        }

        throw PortNotFoundException(id)
    }

    override fun checkNewPorts(): Boolean {
        var result = false
        bundles()
            .forEach { bundle ->
                val hasNewPorts = bundle.adapter.hasNewPorts()
                if (hasNewPorts) {
                    result = true
                }
            }

        return result
    }

    override fun clearNewPortsFlag() {
        bundles()
            .forEach {
                it.adapter.clearNewPortsFlag()
            }
    }

    fun scheduleDiscovery(factoryId: String) {
        factories
            .filter { it.key.owningPluginId == factoryId }
            .flatMap { it.value }
            .forEach {
                startStopScope.launch {
                    discover(it)
                }
            }
    }
}

