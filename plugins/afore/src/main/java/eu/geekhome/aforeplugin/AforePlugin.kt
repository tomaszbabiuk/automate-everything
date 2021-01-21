package eu.geekhome.aforeplugin

import org.pf4j.PluginWrapper
import eu.geekhome.services.hardware.HardwarePlugin
import com.geekhome.common.extensibility.PluginMetadata
import com.geekhome.common.localization.Resource
import eu.geekhome.services.hardware.HardwareAdapterFactory

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