package eu.automateeverything.onewireplugin

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.extensibility.PluginMetadata
import eu.automateeverything.domain.hardware.HardwareAdapter
import eu.automateeverything.domain.hardware.HardwarePlugin
import org.pf4j.PluginWrapper
import java.io.File

class OneWirePlugin(
    wrapper: PluginWrapper)
    : HardwarePlugin(wrapper), PluginMetadata{

    override fun start() {
        println("Starting OneWire plugin")
    }

    override fun stop() {
        println("Stopping OneWire plugin")
    }

    override fun createAdapters(): List<HardwareAdapter<*>> {
        return listSerialPorts()
            .map { createOneWireAdapter(it) }
    }

    private fun createOneWireAdapter(serialPort: String): HardwareAdapter<*> {
        return OneWireAdapter(pluginId, serialPort)
    }

    private fun listSerialPorts(): ArrayList<String> {
        val serialPorts = ArrayList<String>()
        val path = File.separator + "dev"
        val directory = File(path)
        if (directory.exists()) {
            val aliases = directory.listFiles()
            if (aliases != null) {
                for (f in aliases) {
                    if (f.name.startsWith("ttyU")) {
                        val adapterPort = f.absolutePath
                        serialPorts.add(adapterPort)
                    }
                }
            }
        }

        return serialPorts
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
}