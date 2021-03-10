package eu.geekhome.rest.hardware

import eu.geekhome.services.hardware.Connectible
import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.PortDto
import eu.geekhome.services.localization.Resource

class PortDtoMapper {
    fun map(port: Port<*>, factoryId: String, adapterId: String): PortDto {
        val integerValue: Int? = if (port.canRead) { port.read().asInteger() } else null
        val interfaceValue: Resource? = if (port.canRead) { port.read().toFormattedString() } else null
        val connected = if (port is Connectible) { port.connected } else false
        return PortDto(
            port.id,
            factoryId,
            adapterId,
            integerValue,
            interfaceValue,
            port.valueType.simpleName,
            port.canRead,
            port.canWrite,
            connected
        )
    }
}