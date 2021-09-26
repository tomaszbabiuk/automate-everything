package eu.automateeverything.domain.automation

import eu.automateeverything.domain.R
import eu.automateeverything.domain.hardware.PortValue
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