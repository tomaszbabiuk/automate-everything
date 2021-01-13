package eu.geekhome

import eu.geekhome.services.events.EventsSink
import eu.geekhome.services.events.NumberedEventsSink
import eu.geekhome.services.hardware.*
import kotlinx.coroutines.*
import org.pf4j.*
import java.util.*

class HardwareManager(private val _pluginManager: PluginManager) : PluginStateListener {

    private val _factories: MutableMap<HardwareAdapterFactory, List<AdapterBundle>> = HashMap()

    val factories: List<HardwareAdapter>
        get() = _factories
            .entries
            .flatMap { factory: Map.Entry<HardwareAdapterFactory, List<AdapterBundle>> -> factory.value }
            .map { bundle: AdapterBundle -> bundle.adapter }
            .toList()

    init {
        _pluginManager.addPluginStateListener(this)
        reloadAdapters()
    }

    fun discover() {
        println("Discovery started")

        GlobalScope.launch {
            _factories.forEach { (factory: HardwareAdapterFactory, adapterBundles: List<AdapterBundle>) ->
                adapterBundles.forEach { bundle: AdapterBundle ->
                    val builder = PortIdBuilder(factory.id, bundle.adapter.id)
                    bundle.discoveryJob = async {
                        bundle.ports = bundle.adapter.discover(builder, bundle.sink)
                    }
                }
            }
        }

        println("Discovery finished")
    }

    private fun cancelDiscovery() {
        _factories
            .values
            .flatten()
            .forEach {
                it.discoveryJob?.cancel()
            }
    }

    private fun reloadAdapters() {
        _pluginManager
            .plugins
            .filter { plugin: PluginWrapper -> plugin.plugin is HardwarePlugin }
            .map { plugin: PluginWrapper -> (plugin.plugin as HardwarePlugin).factory }
            .forEach { factory: HardwareAdapterFactory ->
                _factories.remove(factory)
                val adaptersInFactory = factory.createAdapters()
                val adapterBundles = adaptersInFactory
                    .map { adapter: HardwareAdapter -> AdapterBundle(adapter, NumberedEventsSink(), ArrayList()) }
                    .toList()
                _factories[factory] = adapterBundles
            }
    }

    override fun pluginStateChanged(event: PluginStateEvent) {
        val isPluginStartedOrStopped = event.pluginState == PluginState.STARTED || event.pluginState == PluginState.STOPPED
        val isHardwarePluginAffected = event.plugin.plugin is HardwarePlugin
        if (isPluginStartedOrStopped && isHardwarePluginAffected) {
            cancelDiscovery()
            reloadAdapters()
            discover()
        }
    }

    data class AdapterBundle(
        internal val adapter: HardwareAdapter,
        internal val sink: EventsSink<String>,
        internal var ports: MutableList<Port<*, *>>
    ) {
        var discoveryJob: Deferred<Unit>? = null
    }
}