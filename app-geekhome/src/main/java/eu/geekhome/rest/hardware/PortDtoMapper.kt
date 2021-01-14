package eu.geekhome.rest.hardware

import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.PortDto

class PortDtoMapper {
    fun map(port: Port<*, *>): PortDto {
        return PortDto(
            port.id,
            port.isShadowed,
            port.isOperational,
            port.nonOperationalTime,
            port.value.toString(),
            port.canRead,
            port.canWrite
        )
    }
}