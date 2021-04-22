package eu.geekhome.coreplugin

import eu.geekhome.services.automation.EvaluationResult
import eu.geekhome.services.automation.IDeviceAutomationUnit
import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.PortValue
import java.util.*

class SensorAutomationUnit<T: PortValue>(
    private val port: Port<T>) :
    IDeviceAutomationUnit<T>() {

    override val usedPortsIds: Array<String>
        get() = arrayOf(port.id)

    override fun calculateInternal(now: Calendar) {
        val value = port.read()
        lastEvaluation = EvaluationResult(
            value = value,
            interfaceValue = value.toFormattedString()
        )
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = false
}
