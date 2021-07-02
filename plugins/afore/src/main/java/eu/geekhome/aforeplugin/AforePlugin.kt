package eu.geekhome.aforeplugin

import eu.geekhome.domain.extensibility.PluginMetadata
import org.pf4j.PluginWrapper
import eu.geekhome.domain.hardware.HardwarePlugin
import eu.geekhome.domain.hardware.HardwareAdapterFactory
import eu.geekhome.domain.langateway.LanGatewayResolver
import eu.geekhome.domain.plugininjection.RequiresLanGatewayResolver
import eu.geekhome.domain.localization.Resource

class AforePlugin(wrapper: PluginWrapper) : HardwarePlugin(wrapper), PluginMetadata, RequiresLanGatewayResolver {

    companion object {
        const val PLUGIN_ID_AFORE = "afore"
    }

    private lateinit var factory: AforeAdapterFactory

    override fun getFactory(): HardwareAdapterFactory {
        return factory
    }

    override fun start() {
        println("Starting AFORE plugin")
    }

    override fun stop() {
        println("Stopping AFORE plugin")
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
    lateinit var lanGatewayResolver: LanGatewayResolver

    override fun injectLanGatewayResolver(resolver: LanGatewayResolver) {
        lanGatewayResolver = resolver
    }

    override fun allFeaturesInjected() {
        factory = AforeAdapterFactory(wrapper.pluginId, lanGatewayResolver)
    }
}