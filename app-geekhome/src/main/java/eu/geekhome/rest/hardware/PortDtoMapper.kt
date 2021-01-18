package eu.geekhome.rest.hardware

import com.geekhome.common.localization.Resource
import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.PortDto

class PortDtoMapper {
    fun map(port: Port<*, *>): PortDto {
        val value: Resource? = if (port.canRead) { port.read().toFormattedString() } else null
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