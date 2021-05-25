package eu.geekhome.aforeplugin

import eu.geekhome.services.hardware.HardwareAdapter
import eu.geekhome.services.hardware.HardwareAdapterFactory

internal class AforeAdapterFactory : HardwareAdapterFactory {

    override fun createAdapters(): List<HardwareAdapter> {
        val result = ArrayList<HardwareAdapter>()
        val adapter = AforeAdapter()
        result.add(adapter)
        return result
    }

    override fun getId(): String {
        return FACTORY_ID
    }

    companion object {
        const val FACTORY_ID = "afore"
    }
}