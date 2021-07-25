package eu.geekhome.domain.automation

import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.domain.events.AutomationUpdateEventData
import eu.geekhome.domain.events.EventsSink

class BroadcastingStateChangeReporter(private val liveEvents: EventsSink) : StateChangeReporter {

    override fun reportDeviceStateChange(deviceUnit: StateDeviceAutomationUnit, instanceDto: InstanceDto) {
        val eventData = AutomationUpdateEventData(deviceUnit, instanceDto, deviceUnit.lastEvaluation)
        liveEvents.broadcastEvent(eventData)
    }
}