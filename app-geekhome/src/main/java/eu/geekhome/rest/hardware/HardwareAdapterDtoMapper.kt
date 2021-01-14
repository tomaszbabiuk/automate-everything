package eu.geekhome.rest.hardware

import eu.geekhome.HardwareManager
import javax.inject.Inject
import eu.geekhome.services.hardware.HardwareAdapterDto

class HardwareAdapterDtoMapper @Inject internal constructor(
    private val portDtoMapper: PortDtoMapper
) {

    fun map(bundle: HardwareManager.AdapterBundle): HardwareAdapterDto {
        val lastError : String? = if (bundle.adapter.lastError != null) bundle.adapter.lastError.toString() else null
        val portsMapped = bundle.ports.map { portDtoMapper.map(it)}

        return HardwareAdapterDto(
            bundle.adapter.state,
            bundle.adapter.lastDiscoveryTime,
            lastError,
            portsMapped)
    }
}