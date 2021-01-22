package eu.geekhome.rest.hardware

import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.PortDto
import eu.geekhome.services.localization.Resource

class PortDtoMapper {
    fun map(port: Port<*, *>, factoryId: String, adapterId: String): PortDto {
        val value: Resource? = if (port.canRead) { port.read().toFormattedString() } else null
        return PortDto(
            port.id,
            factoryId,
            adapterId,
            port.isShadowed,
            value,
            port.valueType.simpleName,
            port.canRead,
            port.canWrite
        )
    }
}