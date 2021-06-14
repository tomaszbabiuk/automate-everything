package eu.geekhome

import eu.geekhome.domain.events.*
import eu.geekhome.domain.hardware.*
import eu.geekhome.domain.repository.Repository
import kotlinx.coroutines.*
import org.pf4j.*
import java.util.*

class HardwareManager(
    private val pluginsCoordinator: PluginsCoordinator,
    private val liveEvents: NumberedEventsSink,
    private val repository: Repository,
) : PluginStateListener, IPortFinder {

    private val factories: MutableMap<HardwareAdapterFactory, List<AdapterBundle>> = HashMap()

    init {
        pluginsCoordinator.addPluginStateListener(this)
    }

    /**
     * This method is called before every automation loop. The method is executed in an async way so it's not
     * blocking automation loop processing. This function will not be called if previous execution is still ongoing.
     */
    suspend fun beforeAutomationLoop(now: Calendar) {
        bundles()
            .forEach { bundle ->
                bundle.adapter.refresh(now)
            }
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
            .filter { factory.id == it.key.id }
            .flatMap { it.value }
            .forEach {
                it.discoveryJob?.cancelAndJoin()
                it.adapter.stop()
            }
    }

    private suspend fun startAdaptersAndDiscover(factory: HardwareAdapterFactory) = coroutineScope {
        factories
            .filter { factory.id == it.key.id }
            .flatMap { it.value }
            .forEach { bundle ->
                bundle.adapter.start(liveEvents)
                bundle.discoveryJob = async {
                    bundle.ports = bundle.adapter.discover(liveEvents)
                    bundle.ports.forEach {
                        val portSnapshot = PortDto(it.id, factory.id, bundle.adapter.id,
                            null, null, it.valueType.simpleName, it.canRead, it.canWrite, false)
                        repository.savePort(portSnapshot)
                        val event = PortUpdateEventData(factory.id, bundle.adapter.id, it)
                        liveEvents.broadcastEvent(event)
                    }
                }
            }
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
            .map { adapter: HardwareAdapter -> AdapterBundle(factory.id, adapter, ArrayList()) }
            .toList()
        factories[factory] = adapterBundles

        startAdaptersAndDiscover(factory)
    }

    override fun pluginStateChanged(event: PluginStateEvent) {
        val isHardwarePluginAffected = event.plugin.plugin is HardwarePlugin
        if (isHardwarePluginAffected) {
            val hardwarePlugin = event.plugin.plugin as HardwarePlugin
            if (event.pluginState == PluginState.STARTED) {
                GlobalScope.launch {
                    addFactory(hardwarePlugin.factory)
                }
            }

            if (event.pluginState == PluginState.STOPPED) {
                GlobalScope.launch {
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
            val port = bundle.ports.firstOrNull { it.id == id }
            if (port != null) {
                return Pair(port, bundle)
            }
        }

        return null
    }

    data class AdapterBundle(
        internal val factoryId: String,
        internal val adapter: HardwareAdapter,
        internal var ports: MutableList<Port<*>>
    ) {
        var discoveryJob: Deferred<Unit>? = null
    }

    override fun <T : PortValue> searchForPort(
        valueType: Class<T>,
        id: String,
        canRead: Boolean,
        canWrite: Boolean
    ): Port<T> {
        val port = factories
            .flatMap { it.value }
            .flatMap { it.ports }
            .find { it.id == id }

        if (port != null && port.valueType == valueType) {
            return port as Port<T>
        }

        throw PortNotFoundException(id)
    }

    override fun checkNewPorts(): Boolean {
        var result = false
        bundles()
            .forEach { bundle ->
                val hasNewPorts = bundle.adapter.newPorts.isNotEmpty()
                if (hasNewPorts) {
                    bundle.adapter.newPorts.forEach { newPort ->
                        val portAlreadyExists = bundle.ports.find { it.id == newPort.id } != null
                        if (!portAlreadyExists) {
                            bundle.ports.add(newPort)
                        }
                    }

                    bundle.adapter.clearNewPorts()
                    result = true
                }
            }

        return result

    }
}

class PortNotFoundException(id: String) : Exception("Cannot find port: $id")
