package eu.automateeverything.domain.automation

import eu.automateeverything.domain.R
import eu.automateeverything.data.hardware.PortValue
import java.util.*

class SensorAutomationNode(
    private val deviceUnit: AutomationUnit<*>
) : ValueNode {

    override fun getValue(now: Calendar): PortValue? {
        if (deviceUnit.lastEvaluation.error != null) {
            val originName = deviceUnit.nameOfOrigin
            throw AutomationErrorException(R.error_other_device_failure(originName),
                deviceUnit.lastEvaluation.error!!)
        }

        return deviceUnit.lastEvaluation.value as PortValue?
    }
}