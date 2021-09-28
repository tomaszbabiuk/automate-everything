package eu.automateeverything.rest.hardware

import eu.automateeverything.domain.hardware.AdapterBundle
import eu.automateeverything.data.hardware.HardwareAdapterDto

class HardwareAdapterDtoMapper {

    fun map(bundle: AdapterBundle): HardwareAdapterDto {
        val lastError : String? = if (bundle.adapter.lastError != null) bundle.adapter.lastError.toString() else null

        return HardwareAdapterDto(
            bundle.adapter.id,
            bundle.owningPluginId,
            bundle.adapter.state,
            bundle.adapter.lastDiscoveryTime,
            lastError,
            bundle.adapter.ports.size)
    }
}