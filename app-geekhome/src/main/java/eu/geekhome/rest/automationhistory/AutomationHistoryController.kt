package eu.geekhome.rest.automationhistory

import eu.geekhome.rest.EventsSinkHolderService
import eu.geekhome.services.events.AutomationUpdateEventData
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("automationhistory")
class AutomationHistoryController @Inject constructor(
    private val eventsSinkHolder: EventsSinkHolderService,
    private val automationHistoryMapper: AutomationHistoryDtoMapper,
) {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getAutomation(): List<AutomationHistoryDto> {
        return eventsSinkHolder
            .instance
            .all()
            .filter { it.data is AutomationUpdateEventData}
            .map {
                val data = it.data as AutomationUpdateEventData
                automationHistoryMapper.map(it.timestamp, data.unit, data.instance)
            }
    }
}