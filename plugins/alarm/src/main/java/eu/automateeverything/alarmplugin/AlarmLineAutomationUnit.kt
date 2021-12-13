package eu.automateeverything.alarmplugin

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.configurable.Duration
import eu.automateeverything.domain.hardware.BinaryInput
import eu.automateeverything.domain.hardware.InputPort
import java.util.*

class AlarmLineAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val inputPort: InputPort<BinaryInput>,
    private val contactType: ContactType,
    delayTime: Duration
) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name, ControlType.States, states, false) {

    private val armingTicks: Long = delayTime.milliseconds
    private var armingStartedAtTicks: Long = 0
    private var lastBreachedTime: Calendar? = null
    private var lineBreached = false

    override fun applyNewState(state: String) {
    }

    override val usedPortsIds: Array<String>
        get() = arrayOf(inputPort.id)

    override val recalculateOnTimeChange = true
    override val recalculateOnPortUpdate = true

    private fun isLineBreached(): Boolean {
        val signal: Boolean = inputPort.read().value
        return contactType === ContactType.NO && signal || contactType === ContactType.NC && !signal
    }

    fun isAlarm(): Boolean {
        return currentState.id == AlarmLineConfigurable.STATE_ALARM
    }

    fun isPrealarm(): Boolean {
        return currentState.id == AlarmLineConfigurable.STATE_PREALARM
    }

    fun isWatching(): Boolean {
        return currentState.id == AlarmLineConfigurable.STATE_WATCHING
    }

    private fun causeForAlarm(): Boolean {
        return isLineBreached()
    }

    @Throws(Exception::class)
    fun arm() {
        changeState(AlarmLineConfigurable.STATE_WATCHING)
    }

    @Throws(Exception::class)
    fun disarm() {
        changeState(AlarmLineConfigurable.STATE_DISARMED)
    }

    @Throws(Exception::class)
    fun alarm() {
        changeState(AlarmLineConfigurable.STATE_ALARM)
    }

    @Throws(Exception::class)
    fun prealarm() {
        changeState(AlarmLineConfigurable.STATE_PREALARM)
    }

    override fun calculateInternal(now: Calendar) {
        if (lineBreached != isLineBreached()) {
            lastBreachedTime = now
        }
        lineBreached = isLineBreached()

        if (isWatching()) {
            if (causeForAlarm()) {
                prealarm()
                armingStartedAtTicks = now.timeInMillis
            }
        }

        if (isPrealarm() && now.timeInMillis > armingStartedAtTicks + armingTicks) {
            alarm()
        }
    }
}
