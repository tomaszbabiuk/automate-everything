package eu.geekhome.rest.automationhistory

import eu.geekhome.domain.events.AutomationStateEventData
import eu.geekhome.domain.events.AutomationUpdateEventData
import eu.geekhome.domain.events.EventsSink
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

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