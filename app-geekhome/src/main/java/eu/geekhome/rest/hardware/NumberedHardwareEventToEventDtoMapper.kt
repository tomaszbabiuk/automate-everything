package eu.geekhome.rest.hardware

import eu.geekhome.services.events.HardwareEvent
import eu.geekhome.services.hardware.DiscoveryEventDto

class NumberedHardwareEventToEventDtoMapper {

    fun map(number: Int, event: HardwareEvent) : DiscoveryEventDto {
        val factoryId = event.factoryId
        val message = event.message
        return DiscoveryEventDto(factoryId, message, number)
    }
}