package eu.automateeverything.coreplugin

import eu.automateeverything.coreplugin.RegulatedPowerWithPresetsDeviceConfigurable.Companion.STATE_MANUAL
import eu.automateeverything.coreplugin.RegulatedPowerWithPresetsDeviceConfigurable.Companion.STATE_OFF
import eu.automateeverything.coreplugin.RegulatedPowerWithPresetsDeviceConfigurable.Companion.STATE_PRESET1
import eu.automateeverything.coreplugin.RegulatedPowerWithPresetsDeviceConfigurable.Companion.STATE_PRESET2
import eu.automateeverything.coreplugin.RegulatedPowerWithPresetsDeviceConfigurable.Companion.STATE_PRESET3
import eu.automateeverything.coreplugin.RegulatedPowerWithPresetsDeviceConfigurable.Companion.STATE_PRESET4
import eu.geekhome.data.automation.State
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.domain.automation.StateChangeReporter
import eu.geekhome.domain.automation.StateDeviceAutomationUnit
import eu.geekhome.domain.hardware.OutputPort
import eu.geekhome.domain.hardware.PowerLevel
import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

class RegulatedPowerWithPresetsDeviceAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instanceDto: InstanceDto,
    name: String,
    private val preset1: Int,
    private val preset2: Int,
    private val preset3: Int,
    private val preset4: Int,
    states: Map<String, State>,
    private val controlPort: OutputPort<PowerLevel>,
) : StateDeviceAutomationUnit(stateChangeReporter, instanceDto, name, states, true) {

    @Throws(Exception::class)
    override fun applyNewState(state: String) {
        when (currentState.id) {
            STATE_PRESET1 -> {
                changePowerLevelIfNeeded(controlPort, PowerLevel(preset1))
            }
            STATE_PRESET2 -> {
                changePowerLevelIfNeeded(controlPort, PowerLevel(preset2))
            }
            STATE_PRESET3 -> {
                changePowerLevelIfNeeded(controlPort, PowerLevel(preset3))
            }
            STATE_PRESET4 -> {
                changePowerLevelIfNeeded(controlPort, PowerLevel(preset4))
            }
            STATE_OFF -> {
                changePowerLevelIfNeeded(controlPort, PowerLevel(0))
            }
        }
    }

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.id)

    override fun calculateInternal(now: Calendar) {
        when (controlPort.read().value) {
            preset1 -> {
                changeState(STATE_PRESET1, null, null)
            }
            preset2 -> {
                changeState(STATE_PRESET2, null, null)
            }
            preset3 -> {
                changeState(STATE_PRESET3, null, null)
            }
            preset4 -> {
                changeState(STATE_PRESET4, null, null)
            }
            0 -> {
                changeState(STATE_OFF, null, null)
            }
            else -> {
                changeState(STATE_MANUAL, null, null)
            }
        }
    }

    init {
        calculateInternal(Calendar.getInstance())
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true
}