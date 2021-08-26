package eu.geekhome.rest.hardware

import eu.geekhome.data.hardware.PortDto
import eu.geekhome.domain.hardware.Port
import eu.geekhome.data.localization.Resource
import java.util.*

class PortDtoMapper {
    fun map(port: Port<*>, factoryId: String, adapterId: String): PortDto {
        val now = Calendar.getInstance()
        val integerValue: Int? = if (port.canRead) { port.tryRead()?.asInteger() } else null
        val interfaceValue: Resource? = if (port.canRead) { port.tryRead()?.toFormattedString() } else null
        val connected = port.checkIfConnected(now)
        return PortDto(
            port.id,
            factoryId,
            adapterId,
            integerValue,
            interfaceValue,
            port.valueClazz.name,
            port.canRead,
            port.canWrite,
            connected
        )
    }
}