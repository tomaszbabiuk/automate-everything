package eu.geekhome.aforeplugin

import eu.geekhome.domain.extensibility.PluginMetadata
import org.pf4j.PluginWrapper
import eu.geekhome.domain.hardware.HardwarePlugin
import eu.geekhome.domain.hardware.HardwareAdapterFactory
import eu.geekhome.domain.localization.Resource

class AforePlugin(wrapper: PluginWrapper?) : HardwarePlugin(wrapper), PluginMetadata {
    private val factory: AforeAdapterFactory = AforeAdapterFactory()

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
}