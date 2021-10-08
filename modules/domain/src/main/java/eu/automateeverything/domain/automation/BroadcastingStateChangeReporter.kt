package eu.automateeverything.domain.automation

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.events.AutomationUpdateEventData
import eu.automateeverything.domain.events.EventsSink

class BroadcastingStateChangeReporter(private val liveEvents: EventsSink) : StateChangeReporter {

    private val listeners = ArrayList<StateChangedListener>()

    override fun reportDeviceStateChange(deviceUnit: StateDeviceAutomationUnitBase, instanceDto: InstanceDto) {
        val eventData = AutomationUpdateEventData(deviceUnit, instanceDto, deviceUnit.lastEvaluation)
        liveEvents.broadcastEvent(eventData)

        listeners.forEach {
            it.onChanged(deviceUnit, instanceDto)
        }
    }

    override fun reportDeviceStateUpdated(deviceUnit: StateDeviceAutomationUnitBase, instanceDto: InstanceDto) {
        val eventData = AutomationUpdateEventData(deviceUnit, instanceDto, deviceUnit.lastEvaluation)
        liveEvents.broadcastEvent(eventData)
    }

    override fun addListener(listener: StateChangedListener) {
        listeners.add(listener)
    }

    override fun removeAllListeners() {
        listeners.clear()
    }
}