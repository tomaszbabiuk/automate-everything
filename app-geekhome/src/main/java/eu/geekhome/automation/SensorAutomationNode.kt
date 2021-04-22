package eu.geekhome.automation

import eu.geekhome.services.automation.IDeviceAutomationUnit
import eu.geekhome.services.hardware.PortValue
import java.util.*

class SensorAutomationNode(
    private val deviceUnit: IDeviceAutomationUnit<*>
) : IValueNode {

    override fun getValue(now: Calendar): PortValue? {
        return deviceUnit.lastEvaluation?.value as PortValue?
    }
}