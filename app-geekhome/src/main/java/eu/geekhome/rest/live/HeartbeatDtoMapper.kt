package eu.geekhome.rest.live

import eu.geekhome.data.heartbeat.HeartbeatDto
import eu.geekhome.domain.events.HeartbeatEventData

class HeartbeatDtoMapper {

    fun map(from: HeartbeatEventData): HeartbeatDto {
        return HeartbeatDto(from.timestamp, from.unreadMessagesCount, from.isAutomationEnabled)
    }
}