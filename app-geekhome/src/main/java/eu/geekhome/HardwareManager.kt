package eu.geekhome

import eu.geekhome.services.events.EventsSink
import eu.geekhome.services.events.NumberedEventsSink
import eu.geekhome.services.hardware.*
import kotlinx.coroutines.*
import org.pf4j.*
import java.util.*

class HardwareManager(pluginManager: PluginManager) : PluginStateListener, IPortFinder {

    private val factories: MutableMap<HardwareAdapterFactory, List<AdapterBundle>> = HashMap()
    val discoverySink: EventsSink<HardwareEvent> = NumberedEventsSink()
    val updateSink: EventsSink<PortUpdateEvent> = NumberedEventsSink()

    init {
        pluginManager.addPluginStateListener(this)
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
        discoverySink.removeRange { it.payload.factoryId == factory.id }

        factories
            .filter { factory.id == it.key.id }
            .flatMap { it.value }
            .forEach { bundle ->
                bundle.adapter.start(updateSink)
                val builder = PortIdBuilder(factory.id, bundle.adapter.id)
                bundle.discoveryJob = async {
                    bundle.ports = bundle.adapter.discover(builder, discoverySink)
                    bundle.ports.forEach {
                        updateSink.broadcastEvent(PortUpdateEvent(factory.id, bundle.adapter.id, it))
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
}

class PortNotFoundException(id: String) : Exception("Cannot find port: $id") {

}
