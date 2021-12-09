package eu.automateeverything.aforeplugin

import eu.automateeverything.domain.extensibility.PluginMetadata
import org.pf4j.PluginWrapper
import eu.automateeverything.domain.hardware.HardwarePlugin
import eu.automateeverything.domain.langateway.LanGatewayResolver
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.hardware.HardwareAdapter

class AforePlugin(
    wrapper: PluginWrapper,
    private val lanGatewayResolver: LanGatewayResolver,
    private val eventsSink: EventsSink) : HardwarePlugin(wrapper), PluginMetadata {

    override fun createAdapters(): List<HardwareAdapter<*>> {
        val result = ArrayList<HardwareAdapter<*>>()
        val adapter = AforeAdapter(pluginId, lanGatewayResolver, eventsSink)
        result.add(adapter)
        return result
    }

    override fun start() {
    }

    override fun stop() {
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
}