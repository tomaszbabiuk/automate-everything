package eu.geekhome

import eu.geekhome.services.events.EventsSink
import eu.geekhome.services.events.NumberedEventsSink
import eu.geekhome.services.hardware.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.pf4j.PluginManager
import org.pf4j.PluginStateEvent
import org.pf4j.PluginStateListener
import org.pf4j.PluginWrapper
import java.util.*
import java.util.stream.Collectors

class HardwareManager(private val _pluginManager: PluginManager) : PluginStateListener {

    private val _factories: MutableMap<HardwareAdapterFactory, List<AdapterBundle>> = HashMap()

    val factories: List<HardwareAdapter>
        get() = _factories
            .entries
            .stream()
            .flatMap { factory: Map.Entry<HardwareAdapterFactory, List<AdapterBundle>> -> factory.value.stream() }
            .map { bundle: AdapterBundle -> bundle.adapter }
            .collect(Collectors.toList())

    init {
        _pluginManager.addPluginStateListener(this)
        reloadAdapters()
    }

    fun discover() {
        GlobalScope.launch {
            _factories.forEach { (factory: HardwareAdapterFactory, adapterBundles: List<AdapterBundle>) ->
                adapterBundles.forEach { bundle: AdapterBundle ->
                    val builder = PortIdBuilder(factory.id, bundle.adapter.id)
                    bundle.adapter.discover(builder, bundle.ports, bundle.sink)
                }
            }
        }

    }

    private fun reloadAdapters() {
        _pluginManager
            .plugins
            .stream()
            .filter { plugin: PluginWrapper -> plugin.plugin is HardwarePlugin }
            .map { plugin: PluginWrapper -> (plugin.plugin as HardwarePlugin).factory }
            .forEach { factory: HardwareAdapterFactory ->
                _factories.remove(factory)
                val adaptersInFactory = factory.createAdapters()
                val adapterBundles = adaptersInFactory
                    .stream()
                    .map { adapter: HardwareAdapter -> AdapterBundle(adapter, NumberedEventsSink(), ArrayList()) }
                    .collect(Collectors.toList())
                _factories[factory] = adapterBundles
            }
    }

    override fun pluginStateChanged(event: PluginStateEvent) {
        reloadAdapters()
    }

    data class AdapterBundle(
        internal val adapter: HardwareAdapter,
        internal val sink: EventsSink<String?>,
        internal val ports: List<Port<*, *>?>
    )
}