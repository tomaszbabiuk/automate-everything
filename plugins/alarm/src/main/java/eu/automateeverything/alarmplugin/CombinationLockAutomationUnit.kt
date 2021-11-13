package eu.automateeverything.alarmplugin

import eu.automateeverything.alarmplugin.CombinationLockConfigurable.Companion.STATE_ARMED
import eu.automateeverything.alarmplugin.CombinationLockConfigurable.Companion.STATE_DISARMED
import eu.automateeverything.alarmplugin.CombinationLockConfigurable.Companion.STATE_LEAVING
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.hardware.BinaryInput
import eu.automateeverything.domain.hardware.InputPort
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.Relay
import java.util.*

class CombinationLockAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val armingPort: InputPort<BinaryInput>,
    private val statusPort: OutputPort<Relay>,
) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name, states, false) {

    var leaving: Boolean = false
        set(value) {
            field = value
            if (value) {
                changeState(STATE_LEAVING)
            }
        }

    private var lastReading = armingPort.read().value
    var isArmed = false
    private var lastToggleTick = 0L

    override fun applyNewState(state: String) {
        when (state) {
            STATE_ARMED -> {
                statusPort.write(Relay(true))
            }
            STATE_DISARMED ->
                statusPort.write(Relay(false))
            STATE_LEAVING -> {
                //status port is set in toggleStatusPort loop
            }
        }
    }

    override val usedPortsIds: Array<String>
        get() = arrayOf(armingPort.id, statusPort.id)

    override val recalculateOnTimeChange = true
    override val recalculateOnPortUpdate = true

    @Throws(Exception::class)
    fun arm() {
        if (!isArmed) {
            isArmed = true
            changeState(STATE_ARMED)
        }
    }

    @Throws(Exception::class)
    fun disarm() {
        if (isArmed) {
            isArmed = true
            changeState(STATE_DISARMED)
        }
    }

    override fun calculateInternal(now: Calendar) {
        val newReading: Boolean = armingPort.read().value
        if (lastReading != newReading && !newReading) {
            lastReading = newReading

            if (isArmed) {
                disarm()
            } else {
                arm()
            }
        }

        toggleStatusPort(now)
    }

    private fun toggleStatusPort(now: Calendar) {
        val oneSecondElapsed = now.timeInMillis - lastToggleTick > 1000
        if (leaving && oneSecondElapsed) {
            lastToggleTick = now.timeInMillis
            val relayState = statusPort.read().value
            statusPort.write(Relay(!relayState))
        }
    }
}
