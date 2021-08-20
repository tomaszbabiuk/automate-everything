package eu.geekhome.domain.automation

import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.domain.events.AutomationUpdateEventData
import eu.geekhome.domain.events.EventsSink

class BroadcastingStateChangeReporter(private val liveEvents: EventsSink) : StateChangeReporter {

    private val listeners = ArrayList<StateChangedListener>()

    override fun reportDeviceStateChange(deviceUnit: StateDeviceAutomationUnit, instanceDto: InstanceDto) {
        val eventData = AutomationUpdateEventData(deviceUnit, instanceDto, deviceUnit.lastEvaluation)
        liveEvents.broadcastEvent(eventData)

        listeners.forEach {
            it.onChanged(deviceUnit, instanceDto)
        }
    }

    override fun reportDeviceStateUpdated(deviceUnit: StateDeviceAutomationUnit, instanceDto: InstanceDto) {
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