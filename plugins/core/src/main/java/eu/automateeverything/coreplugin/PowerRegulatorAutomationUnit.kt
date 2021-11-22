package eu.automateeverything.coreplugin

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.EvaluationResult
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.PowerLevel
import java.util.*

class PowerRegulatorAutomationUnit(
    instanceDto: InstanceDto,
    name: String,
    private val controlPort: OutputPort<PowerLevel>,
    private val readOnly: Boolean
) : AutomationUnit<PowerLevel>(name) {

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.id)

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true

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
}