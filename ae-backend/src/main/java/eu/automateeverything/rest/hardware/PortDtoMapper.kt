package eu.automateeverything.rest.hardware

import eu.automateeverything.data.hardware.PortDto
import eu.automateeverything.domain.hardware.Port
import eu.automateeverything.data.localization.Resource
import java.math.BigDecimal
import java.util.*

class PortDtoMapper {
    fun map(port: Port<*>, factoryId: String, adapterId: String): PortDto {
        val now = Calendar.getInstance()
        val decimalValue: BigDecimal? = if (port.canRead) { port.tryRead()?.asDecimal() } else null
        val interfaceValue: Resource? = if (port.canRead) { port.tryRead()?.toFormattedString() } else null
        val connected = port.checkIfConnected(now)
        return PortDto(
            port.id,
            factoryId,
            adapterId,
            decimalValue,
            interfaceValue,
            port.valueClazz.name,
            port.canRead,
            port.canWrite,
            connected
        )
    }
}