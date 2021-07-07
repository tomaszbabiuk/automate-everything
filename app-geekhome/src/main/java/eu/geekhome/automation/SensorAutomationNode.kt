package eu.geekhome.automation

import eu.geekhome.domain.R
import eu.geekhome.domain.automation.AutomationErrorException
import eu.geekhome.domain.automation.DeviceAutomationUnit
import eu.geekhome.domain.hardware.PortValue
import java.util.*

class SensorAutomationNode(
    private val deviceUnit: DeviceAutomationUnit<*>
) : IValueNode {

    override fun getValue(now: Calendar): PortValue? {
        if (deviceUnit.lastEvaluation.error != null) {
            val originName = deviceUnit.nameOfOrigin
            if (originName != null) {
                throw AutomationErrorException(R.error_other_device_failure(originName),
                    deviceUnit.lastEvaluation.error!!)
            } else {
                throw AutomationErrorException(R.error_other_device_failure,
                    deviceUnit.lastEvaluation.error!!)
            }
        }

        return deviceUnit.lastEvaluation.value as PortValue?
    }
}