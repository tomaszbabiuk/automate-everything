package eu.geekhome.rest.hardware

import eu.geekhome.HardwareManager
import eu.geekhome.services.hardware.HardwareAdapterDto

class HardwareAdapterDtoMapper {

    fun map(bundle: HardwareManager.AdapterBundle): HardwareAdapterDto {
        val lastError : String? = if (bundle.adapter.lastError != null) bundle.adapter.lastError.toString() else null

        return HardwareAdapterDto(
            bundle.adapter.id,
            bundle.factoryId,
            bundle.adapter.state,
            bundle.adapter.lastDiscoveryTime,
            lastError,
            bundle.ports.size)
    }
}