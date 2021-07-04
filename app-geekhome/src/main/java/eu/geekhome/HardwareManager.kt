package eu.geekhome

import eu.geekhome.domain.events.*
import eu.geekhome.domain.events.LiveEventsHelper.broadcastDiscoveryEvent
import eu.geekhome.domain.events.LiveEventsHelper.broadcastPortUpdateEvent
import eu.geekhome.domain.hardware.*
import eu.geekhome.domain.repository.Repository
import eu.geekhome.domain.repository.SettingsDto
import kotlinx.coroutines.*
import org.pf4j.*
import java.util.*

class HardwareManager(
    private val pluginsCoordinator: PluginsCoordinator,
    private val liveEvents: NumberedEventsSink,
    private val repository: Repository,
) : PluginStateListener, IPortFinder {

    private val factories: MutableMap<HardwareAdapterFactory, List<AdapterBundle>> = HashMap()
    private val hardwareManagerScope = CoroutineScope(Dispatchers.IO)

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
                bundle.adapter.start(liveEvents, extractPluginSettings(bundle.owningPluginId))
                discover(bundle)
            }
    }

    private suspend fun discover(bundle: AdapterBundle) = coroutineScope {
        if (bundle.discoveryJob != null && bundle.discoveryJob!!.isActive) {
            broadcastDiscoveryEvent(liveEvents, bundle.owningPluginId, "Previous discovery is still pending, try again later")
        } else {
            bundle.discoveryJob = async {
                bundle.adapter.discover(liveEvents)
                bundle.adapter.ports.values.forEach {
                    val portSnapshot = PortDto(it.id, bundle.owningPluginId, bundle.adapter.id,
                        null, null, it.valueType.simpleName, it.canRead, it.canWrite, false)
                    repository.updatePort(portSnapshot)
                    broadcastPortUpdateEvent(liveEvents, bundle.owningPluginId, bundle.adapter.id, it)
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
                hardwareManagerScope.launch {
                    addFactory(hardwarePlugin.factory)
                }
            }

            if (event.pluginState == PluginState.STOPPED) {
                hardwareManagerScope.launch {
                    removeFactory(hardwarePlugin.factory)
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

    data class AdapterBundle(
        internal val owningPluginId: String,
        internal val adapter: HardwareAdapter<*>,
    ) {
        var discoveryJob: Deferred<Unit>? = null
    }

    override fun <T : PortValue> searchForAnyPort(
        valueType: Class<T>,
        id: String
    ): Port<T> {
        val port = factories
            .flatMap { it.value }
            .flatMap { it.adapter.ports.values }
            .find { it.id == id }

        if (port != null && port.valueType == valueType) {
            return port as Port<T>
        }

        throw PortNotFoundException(id)
    }

    override fun <T : PortValue> searchForInputPort(valueType: Class<T>, id: String): InputPort<T> {
        val anyPort = searchForAnyPort(valueType, id)
        if (anyPort is InputPort<T>) {
            return anyPort
        }

        throw PortNotFoundException(id)
    }

    override fun <T : PortValue> searchForOutputPort(valueType: Class<T>, id: String): OutputPort<T> {
        val anyPort = searchForAnyPort(valueType, id)
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
                    bundle.adapter.clearNewPorts()
                    result = true
                }
            }

        return result
    }

    fun scheduleDiscovery(factoryId: String) {
        factories
            .filter { it.key.owningPluginId == factoryId }
            .flatMap { it.value }
            .forEach {
                hardwareManagerScope.launch {
                    discover(it)
                }
            }
    }
}

class PortNotFoundException(id: String) : Exception("Cannot find port: $id")
