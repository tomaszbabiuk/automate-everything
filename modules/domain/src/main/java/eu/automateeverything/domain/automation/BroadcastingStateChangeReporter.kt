package eu.automateeverything.domain.automation

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.events.AutomationUpdateEventData
import eu.automateeverything.domain.events.EventsSink

class BroadcastingStateChangeReporter(private val liveEvents: EventsSink) : StateChangeReporter {

    private val listeners = ArrayList<StateChangedListener>()

    override fun reportDeviceStateChange(deviceUnit: StateDeviceAutomationUnit, instance: InstanceDto) {
        val eventData = AutomationUpdateEventData(deviceUnit, instance, deviceUnit.lastEvaluation)
        liveEvents.broadcastEvent(eventData)

        listeners.forEach {
            it.onStateChanged(deviceUnit, instance)
        }
    }

    override fun reportDeviceValueChange(deviceUnit: ControllerAutomationUnit<*>, instance: InstanceDto) {
        val eventData = AutomationUpdateEventData(deviceUnit, instance, deviceUnit.lastEvaluation)
        liveEvents.broadcastEvent(eventData)

        listeners.forEach {
            it.onValueChanged(deviceUnit, instance)
        }
    }

    override fun reportDeviceUpdated(deviceUnit: AutomationUnit<*>, instance: InstanceDto) {
        val eventData = AutomationUpdateEventData(deviceUnit, instance, deviceUnit.lastEvaluation)
        liveEvents.broadcastEvent(eventData)
    }

    override fun addListener(listener: StateChangedListener) {
        listeners.add(listener)
    }

    override fun removeAllListeners() {
        listeners.clear()
    }
}