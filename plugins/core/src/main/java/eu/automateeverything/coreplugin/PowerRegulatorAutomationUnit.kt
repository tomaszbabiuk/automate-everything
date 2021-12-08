package eu.automateeverything.coreplugin

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.PowerLevel
import java.math.BigDecimal

class PowerRegulatorAutomationUnit(
    nameOfOrigin: String,
    instanceDto: InstanceDto,
    controlPort: OutputPort<PowerLevel>,
    readOnly: Boolean,
    stateChangeReporter: StateChangeReporter,
) : SinglePortControllerAutomationUnit<PowerLevel>(nameOfOrigin, instanceDto, controlPort, readOnly, stateChangeReporter) {
    override val min: BigDecimal = BigDecimal.ZERO
    override val max: BigDecimal = 100.0.toBigDecimal()
    override val step: BigDecimal = 1.toBigDecimal()
    override val valueClazz: Class<PowerLevel> = PowerLevel::class.java
}

