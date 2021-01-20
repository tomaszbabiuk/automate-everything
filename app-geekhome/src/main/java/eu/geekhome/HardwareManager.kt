package eu.geekhome

import eu.geekhome.services.events.EventsSink
import eu.geekhome.services.events.NumberedEventsListener
import eu.geekhome.services.events.NumberedEventsSink
import eu.geekhome.services.hardware.*
import kotlinx.coroutines.*
import org.pf4j.*
import java.util.*

class HardwareManager(pluginManager: PluginManager) : PluginStateListener {

    private val factories: MutableMap<HardwareAdapterFactory, List<AdapterBundle>> = HashMap()
    val sink: EventsSink<HardwareEvent> = NumberedEventsSink()

    init {
        pluginManager.addPluginStateListener(this)
    }

    fun discover() {
        println("Discovery started")

        GlobalScope.launch {
            factories.forEach { (factory: HardwareAdapterFactory, adapterBundles: List<AdapterBundle>) ->
                adapterBundles.forEach { bundle: AdapterBundle ->
                    val builder = PortIdBuilder(factory.id, bundle.adapter.id)
                    bundle.discoveryJob = async {
                        bundle.ports = bundle.adapter.discover(builder, sink)
                    }
                }
            }
        }

        println("Discovery finished")
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
            .forEach {
                it.adapter.start()
                val builder = PortIdBuilder(factory.id, it.adapter.id)
                it.discoveryJob = async {
                    it.ports = it.adapter.discover(builder, sink)
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

            if (event.pluginState == PluginState.STARTED) {
                GlobalScope.launch {
                    removeFactory(hardwarePlugin.factory)
                }
            }
        }
    }

    fun bundles(): List<AdapterBundle> {
        return factories.values.flatten()
    }

    data class AdapterBundle(
        internal val factoryId: String,
        internal val adapter: HardwareAdapter,
        internal var ports: MutableList<Port<*, *>>
    ) {
        var discoveryJob: Deferred<Unit>? = null
    }
}