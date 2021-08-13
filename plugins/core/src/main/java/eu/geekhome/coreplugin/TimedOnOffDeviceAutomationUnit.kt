package eu.geekhome.coreplugin

import eu.geekhome.coreplugin.TimedOnOffDeviceConfigurable.Companion.STATE_OFF
import eu.geekhome.coreplugin.TimedOnOffDeviceConfigurable.Companion.STATE_OFF_BREAK
import eu.geekhome.coreplugin.TimedOnOffDeviceConfigurable.Companion.STATE_ON
import eu.geekhome.coreplugin.TimedOnOffDeviceConfigurable.Companion.STATE_ON_COUNTING
import eu.geekhome.data.automation.NextStatesDto
import eu.geekhome.data.automation.State
import eu.geekhome.data.automation.StateType
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.domain.automation.StateChangeReporter
import eu.geekhome.domain.automation.StateDeviceAutomationUnit
import eu.geekhome.domain.configurable.Duration
import eu.geekhome.domain.configurable.StateDeviceConfigurable.Companion.STATE_UNKNOWN
import eu.geekhome.domain.hardware.OutputPort
import eu.geekhome.domain.hardware.Relay
import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

class TimedOnOffDeviceAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instanceDto: InstanceDto,
    name: String,
    private val minWorkingTime: Duration,
    private val maxWorkingTime: Duration,
    private val breakTime: Duration,
    states: Map<String, State>,
    private val controlPort: OutputPort<Relay>,
) : StateDeviceAutomationUnit(stateChangeReporter, instanceDto, name, states, false) {

    var onSince = 0L
    var offSince = 0L

    @Throws(Exception::class)
    override fun applyNewState(state: String) {
        if (currentState.id == STATE_ON || currentState.id == STATE_ON_COUNTING) {
            changeRelayStateIfNeeded(controlPort, Relay(true))
            onSince = Calendar.getInstance().timeInMillis
            offSince = 0L
        } else if (currentState.id == STATE_OFF || currentState.id == STATE_OFF_BREAK) {
            changeRelayStateIfNeeded(controlPort, Relay(false))
            onSince = 0L
            offSince = Calendar.getInstance().timeInMillis
        }
    }

    private fun statesExcept(currentState: State, excludedStates: Array<String>): NextStatesDto {
        val nextStates = states
            .map { it.value }
            .filter { it.type != StateType.ReadOnly }
            .filter { it.id !in excludedStates }
        return NextStatesDto(nextStates, currentState.id, requiresExtendedWidth)
    }

    @Suppress("SENSELESS_COMPARISON")
    override fun buildNextStates(state: State): NextStatesDto {
        if (state.id == STATE_OFF) {
            return if (minWorkingTime != null && minWorkingTime.seconds > 0) {
                statesExcept(state, arrayOf(STATE_ON))
            } else {
                statesExcept(state, arrayOf(STATE_ON_COUNTING))
            }
        }

        if (state.id == STATE_ON) {
            return statesExcept(state, arrayOf(STATE_ON_COUNTING))
        }

        if (state.id == STATE_ON_COUNTING) {
            return statesExcept(state, arrayOf(STATE_ON))
        }

        if (state.id == STATE_OFF_BREAK) {
            return statesExcept(state, arrayOf(STATE_ON, STATE_ON_COUNTING))
        }

        return super.buildNextStates(state)
    }

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.id)

    override fun calculateInternal(now: Calendar) {
        fun changeStateToOnOrOnCounting() {
            if (minWorkingTime.seconds > 0) {
                changeState(STATE_ON_COUNTING)
            } else {
                changeState(STATE_ON)
            }
        }

        val portReading = controlPort.read().value
        val onTime = if (portReading) {
                now.timeInMillis - onSince
            } else {
                0
            }

        val offTime = if (portReading) {
            0
        } else {
            now.timeInMillis - offSince
        }

        when (currentState.id) {
            STATE_UNKNOWN -> {
                if (portReading) {
                    changeStateToOnOrOnCounting()
                } else {
                    changeState(STATE_OFF)
                }
            }

            STATE_ON_COUNTING -> {
                val minTimeElapsed = onTime > minWorkingTime.seconds * 1000
                if (minTimeElapsed) {
                    changeState(STATE_ON)
                }
            }

            STATE_ON -> {
                if (maxWorkingTime.seconds > 0) {
                    val shouldBeDisabled = onTime > maxWorkingTime.seconds * 1000

                    if (shouldBeDisabled) {
                        if (breakTime.seconds > 0) {
                            changeState(STATE_OFF_BREAK)
                        } else {
                            changeState(STATE_OFF)
                        }
                    }
                }
            }

            STATE_OFF_BREAK -> {
                if (breakTime.seconds > 0) {
                    val shouldBeEnabled = offTime > breakTime.seconds * 1000

                    if (shouldBeEnabled) {
                        changeState(STATE_ON)
                    }
                }
            }
        }

        if (portReading) {
            when (currentState.id) {
                STATE_OFF_BREAK, STATE_OFF -> {
                    changeStateToOnOrOnCounting()
                }
            }
        } else {
            when (currentState.id) {
                STATE_ON, STATE_ON_COUNTING -> {
                    changeState(STATE_OFF)
                }
            }
        }
    }

    init {
        calculateInternal(Calendar.getInstance())
    }

    override val recalculateOnTimeChange = true
    override val recalculateOnPortUpdate = true
}