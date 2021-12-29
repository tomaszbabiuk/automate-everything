package eu.automateeverything.rest.hardware

import eu.automateeverything.domain.events.DiscoveryEventData
import eu.automateeverything.data.hardware.DiscoveryEventDto

class NumberedHardwareEventToEventDtoMapper {

    fun map(number: Int, event: DiscoveryEventData) : DiscoveryEventDto {
        val factoryId = event.factoryId
        val message = event.message
        return DiscoveryEventDto(factoryId, message, number)
    }
}