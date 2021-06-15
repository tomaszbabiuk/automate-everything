package eu.geekhome.aforeplugin

import eu.geekhome.domain.hardware.HardwareAdapter
import eu.geekhome.domain.hardware.HardwareAdapterFactory

internal class AforeAdapterFactory(override val owningPluginId: String) : HardwareAdapterFactory {

    override fun createAdapters(): List<HardwareAdapter> {
        val result = ArrayList<HardwareAdapter>()
        val adapter = AforeAdapter(owningPluginId)
        result.add(adapter)
        return result
    }
}