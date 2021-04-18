package eu.geekhome.rest.hardware

import eu.geekhome.services.events.DiscoveryEventData
import eu.geekhome.services.hardware.DiscoveryEventDto

class NumberedHardwareEventToEventDtoMapper {

    fun map(number: Int, event: DiscoveryEventData) : DiscoveryEventDto {
        val factoryId = event.factoryId
        val message = event.message
        return DiscoveryEventDto(factoryId, message, number)
    }
}