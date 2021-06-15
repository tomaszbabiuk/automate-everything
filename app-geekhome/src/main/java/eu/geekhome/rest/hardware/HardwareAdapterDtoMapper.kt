package eu.geekhome.rest.hardware

import eu.geekhome.HardwareManager
import eu.geekhome.domain.hardware.HardwareAdapterDto

class HardwareAdapterDtoMapper {

    fun map(bundle: HardwareManager.AdapterBundle): HardwareAdapterDto {
        val lastError : String? = if (bundle.adapter.lastError != null) bundle.adapter.lastError.toString() else null

        return HardwareAdapterDto(
            bundle.adapter.id,
            bundle.owningPluginId,
            bundle.adapter.state,
            bundle.adapter.lastDiscoveryTime,
            lastError,
            bundle.ports.size)
    }
}