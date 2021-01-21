package eu.geekhome.aforeplugin

import eu.geekhome.services.extensibility.PluginMetadata
import org.pf4j.PluginWrapper
import eu.geekhome.services.hardware.HardwarePlugin
import eu.geekhome.services.hardware.HardwareAdapterFactory
import eu.geekhome.services.localization.Resource

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

    override fun getName(): Resource {
        return R.plugin_name
    }

    override fun getDescription(): Resource {
        return R.plugin_description
    }

    companion object {
        const val PLUGIN_ID_MQTT = "mqtt"
        const val PLUGIN_ID_AFORE = "afore"
    }

}