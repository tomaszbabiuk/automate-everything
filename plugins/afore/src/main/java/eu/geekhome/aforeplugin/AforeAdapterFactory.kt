package eu.geekhome.aforeplugin

import eu.geekhome.domain.hardware.HardwareAdapter
import eu.geekhome.domain.hardware.HardwareAdapterFactory
import eu.geekhome.domain.langateway.LanGatewayResolver

internal class AforeAdapterFactory(
    override val owningPluginId: String,
    private val lanGatewayResolver: LanGatewayResolver) : HardwareAdapterFactory {

    override fun createAdapters(): List<HardwareAdapter> {
        val result = ArrayList<HardwareAdapter>()
        val adapter = AforeAdapter(owningPluginId, lanGatewayResolver)
        result.add(adapter)
        return result
    }
}