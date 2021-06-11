package eu.geekhome.rest.hardware

import eu.geekhome.domain.hardware.IConnectible
import eu.geekhome.domain.hardware.Port
import eu.geekhome.domain.hardware.PortDto
import eu.geekhome.domain.localization.Resource
import java.util.*

class PortDtoMapper {
    fun map(port: Port<*>, factoryId: String, adapterId: String): PortDto {
        val now = Calendar.getInstance()
        val integerValue: Int? = if (port.canRead) { port.read().asInteger() } else null
        val interfaceValue: Resource? = if (port.canRead) { port.read().toFormattedString() } else null
        val connected = if (port is IConnectible) { port.checkIfConnected(now) } else false
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