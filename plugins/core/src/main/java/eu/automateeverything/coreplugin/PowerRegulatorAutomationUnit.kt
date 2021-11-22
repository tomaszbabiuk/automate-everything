package eu.automateeverything.coreplugin

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.EvaluationResult
import eu.automateeverything.domain.automation.PowerDeviceAutomationUnit
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.PowerLevel
import java.util.*

class PowerRegulatorAutomationUnit(
    instanceDto: InstanceDto,
    nameOfOrigin: String,
    private val controlPort: OutputPort<PowerLevel>,
    readOnly: Boolean
) : PowerDeviceAutomationUnit(nameOfOrigin) {

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.id)

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true

    override fun changePowerLevel(level: Int, actor: String?) {
        val newPowerLevel = PowerLevel(level)
        controlPort.write(newPowerLevel)
        lastEvaluation = buildEvaluationResult(newPowerLevel)
    }

    override var lastEvaluation = buildEvaluationResult(controlPort.read())

    override fun calculateInternal(now: Calendar) {
    }

    private fun buildEvaluationResult(power: PowerLevel) : EvaluationResult<PowerLevel> {
        return EvaluationResult(
            interfaceValue = Resource.createUniResource("${power.value} %"),
            value = power,
            isSignaled = power.value > 0,
            descriptions = lastNotes.values.toList()
        )
    }

    override val controlType = if (readOnly) ControlType.NA else ControlType.PowerLevel
}