package eu.geekhome.domain.heartbeat

import eu.geekhome.domain.events.HeartbeatEventData

class HeartbeatDtoMapper {

    fun map(heartbeatEventData: HeartbeatEventData): HeartbeatDto {
        return HeartbeatDto(heartbeatEventData.timestamp)
    }
}