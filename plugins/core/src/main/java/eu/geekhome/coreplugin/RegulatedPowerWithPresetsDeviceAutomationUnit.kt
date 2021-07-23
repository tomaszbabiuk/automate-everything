package eu.geekhome.coreplugin

import eu.geekhome.coreplugin.RegulatedPowerWithPresetsDeviceConfigurable.Companion.STATE_MANUAL
import eu.geekhome.coreplugin.RegulatedPowerWithPresetsDeviceConfigurable.Companion.STATE_OFF
import eu.geekhome.coreplugin.RegulatedPowerWithPresetsDeviceConfigurable.Companion.STATE_PRESET1
import eu.geekhome.coreplugin.RegulatedPowerWithPresetsDeviceConfigurable.Companion.STATE_PRESET2
import eu.geekhome.coreplugin.RegulatedPowerWithPresetsDeviceConfigurable.Companion.STATE_PRESET3
import eu.geekhome.coreplugin.RegulatedPowerWithPresetsDeviceConfigurable.Companion.STATE_PRESET4
import eu.geekhome.data.automation.ControlMode
import eu.geekhome.data.automation.State
import eu.geekhome.domain.automation.StateDeviceAutomationUnit
import eu.geekhome.domain.hardware.OutputPort
import eu.geekhome.domain.hardware.PowerLevel
import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

class RegulatedPowerWithPresetsDeviceAutomationUnit(
    name: String,
    private val preset1: Int,
    private val preset2: Int,
    private val preset3: Int,
    private val preset4: Int,
    states: Map<String, State>,
    initialState: String,
    private val controlPort: OutputPort<PowerLevel>,
) : StateDeviceAutomationUnit(name, states, initialState) {

    init {
        setCurrentState(STATE_OFF)
    }

    override val requiresExtendedWidth = true

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
        val value = controlPort.read().value

        if (value != preset1 || value != preset2 || value != preset3 || value != preset4 || value != 0) {
            changeState(STATE_MANUAL, ControlMode.Manual, null, null)
        }
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true
}