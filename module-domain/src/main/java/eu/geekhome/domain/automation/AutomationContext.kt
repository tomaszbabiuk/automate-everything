package eu.geekhome.domain.automation

import eu.geekhome.domain.configurable.Configurable
import eu.geekhome.domain.events.AutomationUpdateEventData
import eu.geekhome.domain.events.NumberedEventsSink
import eu.geekhome.data.instances.InstanceDto

class AutomationContext(
    val instanceDto: InstanceDto,
    val thisDevice: Configurable?,
    val automationUnitsCache: Map<Long, DeviceAutomationUnit<*>>,
    val evaluationUnitsCache: Map<Long, IEvaluableAutomationUnit>,
    val blocksCache: List<BlockFactory<*>>,
    private val liveEvents: NumberedEventsSink
) {
    fun reportDeviceStateChange(deviceUnit: StateDeviceAutomationUnit) {
        val eventData = AutomationUpdateEventData(deviceUnit, instanceDto, deviceUnit.lastEvaluation)
        liveEvents.broadcastEvent(eventData)

        //TODO:
        //send this change to other change state triggers
    }
}