package eu.geekhome.rest.hardware

import eu.geekhome.services.events.NumberedEvent
import eu.geekhome.services.hardware.HardwareEvent
import eu.geekhome.services.hardware.HardwareEventDto

class NumberedHardwareEventToEventDtoMapper {

    fun map(event: NumberedEvent<HardwareEvent>) : HardwareEventDto {
        val factoryId = event.payload.factoryId
        val message =event.payload.message
        val no = event.no
        return HardwareEventDto(factoryId, message, no)
    }
}