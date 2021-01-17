package eu.geekhome.rest.hardware

import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.PortDto

class PortDtoMapper {
    fun map(port: Port<*, *>): PortDto {
        val value = if (port.canRead) { port.read().toString() } else null
        return PortDto(
            port.id,
            port.isShadowed,
            port.isOperational,
            port.nonOperationalTime,
            value,
            port.canRead,
            port.canWrite
        )
    }
}