package eu.geekhome.aforeplugin

import eu.geekhome.domain.extensibility.PluginMetadata
import org.pf4j.PluginWrapper
import eu.geekhome.domain.hardware.HardwarePlugin
import eu.geekhome.domain.langateway.LanGatewayResolver
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.hardware.HardwareAdapter

class AforePlugin(
    wrapper: PluginWrapper,
    private val lanGatewayResolver: LanGatewayResolver) : HardwarePlugin(wrapper), PluginMetadata {

    companion object {
        const val PLUGIN_ID_AFORE = "afore"
    }

    override val owningPluginId: String
        get() = PLUGIN_ID_AFORE

    override fun createAdapters(): List<HardwareAdapter<*>> {
        val result = ArrayList<HardwareAdapter<*>>()
        val adapter = AforeAdapter(owningPluginId, lanGatewayResolver)
        result.add(adapter)
        return result
    }

    override fun start() {
        println("Starting AFORE plugin")
    }

    override fun stop() {
        println("Stopping AFORE plugin")
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
}