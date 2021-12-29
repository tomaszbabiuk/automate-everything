package eu.automateeverything.rest.automationhistory

import eu.automateeverything.data.automationhistory.AutomationHistoryDto
import eu.automateeverything.domain.events.AutomationStateEventData
import eu.automateeverything.domain.events.AutomationUpdateEventData
import eu.automateeverything.domain.events.EventsSink
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("automationhistory")
class AutomationHistoryController @Inject constructor(
    private val eventsSink: EventsSink,
    private val automationHistoryMapper: AutomationHistoryDtoMapper,
) {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getAutomation(): List<AutomationHistoryDto> {
        return eventsSink
            .all()
            .filter { it.data is AutomationUpdateEventData || it.data is AutomationStateEventData }
            .mapNotNull {
                when (it.data) {
                    is AutomationUpdateEventData -> {
                        val data = it.data as AutomationUpdateEventData
                        automationHistoryMapper.map(it.timestamp, data, it.number)
                    }
                    is AutomationStateEventData -> {
                        val data = it.data as AutomationStateEventData
                        automationHistoryMapper.map(it.timestamp, data, it.number)
                    }
                    else -> {
                        null
                    }
                }
            }
    }
}