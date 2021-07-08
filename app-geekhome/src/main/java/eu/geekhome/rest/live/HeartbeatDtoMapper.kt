package eu.geekhome.rest.live

import eu.geekhome.data.heartbeat.HeartbeatDto
import eu.geekhome.domain.events.HeartbeatEventData

class HeartbeatDtoMapper {

    fun map(heartbeatEventData: HeartbeatEventData): HeartbeatDto {
        return HeartbeatDto(heartbeatEventData.timestamp)
    }
}