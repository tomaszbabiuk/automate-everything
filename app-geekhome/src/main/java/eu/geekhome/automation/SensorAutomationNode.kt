package eu.geekhome.automation

import eu.geekhome.services.automation.DeviceAutomationUnit
import eu.geekhome.services.hardware.PortValue
import java.util.*

class SensorAutomationNode(
    private val deviceUnit: DeviceAutomationUnit<*>
) : IValueNode {

    override fun getValue(now: Calendar): PortValue? {
        return deviceUnit.lastEvaluation?.value as PortValue?
    }
}