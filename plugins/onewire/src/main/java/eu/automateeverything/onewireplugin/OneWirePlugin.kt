package eu.automateeverything.onewireplugin

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.extensibility.PluginMetadata
import eu.automateeverything.domain.hardware.HardwareAdapter
import eu.automateeverything.domain.hardware.HardwarePlugin
import eu.automateeverything.domain.settings.SettingsResolver
import org.pf4j.PluginWrapper

class OneWirePlugin(
    wrapper: PluginWrapper,
    private val eventsSink: EventsSink,
    private val settingsResolver: SettingsResolver)
    : HardwarePlugin(wrapper), PluginMetadata{

    override fun start() {
        println("Starting OneWire plugin")
    }

    override fun stop() {
        println("Stopping OneWire plugin")
    }

    override fun createAdapters(): List<HardwareAdapter<*>> {
        val settings = settingsResolver.resolve()
        val ds2408AsRelays = settings
            .firstOrNull()
            ?.fields
            ?.get(DS2408RolesSettingGroup.FIELD_ADDRESSES_OF_RELAYS)
            ?.replace(";",",")
            ?.replace(" ","")
            ?.split(",") ?: listOf()


        return listSerialPorts()
            .map { createOneWireAdapter(it, ds2408AsRelays) }
    }

    private fun createOneWireAdapter(serialPort: String, ds2408AsRelays: List<String>): HardwareAdapter<*> {
        return OneWireAdapter(pluginId, serialPort, eventsSink, ds2408AsRelays)
    }

    private fun listSerialPorts(): List<String> {
//        val serialPorts = ArrayList<String>()
//        val path = File.separator + "dev"
//        val directory = File(path)
//        if (directory.exists()) {
//            val aliases = directory.listFiles()
//            if (aliases != null) {
//                for (f in aliases) {
//                    if (f.name.startsWith("ttyU")) {
//                        val adapterPort = f.absolutePath
//                        serialPorts.add(adapterPort)
//                    }
//                }
//            }
//        }
//
//        return serialPorts

        return listOf("COM2")
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description

    override val settingGroups = listOf(DS2408RolesSettingGroup())
}