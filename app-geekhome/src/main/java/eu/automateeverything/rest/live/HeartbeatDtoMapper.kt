package eu.automateeverything.rest.live

import eu.automateeverything.data.heartbeat.HeartbeatDto
import eu.automateeverything.domain.events.HeartbeatEventData

class HeartbeatDtoMapper {

    fun map(from: HeartbeatEventData): HeartbeatDto {
        return HeartbeatDto(from.timestamp, from.unreadMessagesCount, from.isAutomationEnabled)
    }
}