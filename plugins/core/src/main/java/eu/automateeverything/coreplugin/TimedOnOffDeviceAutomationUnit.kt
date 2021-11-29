package eu.automateeverything.coreplugin

import eu.automateeverything.coreplugin.TimedOnOffDeviceConfigurable.Companion.STATE_OFF
import eu.automateeverything.coreplugin.TimedOnOffDeviceConfigurable.Companion.STATE_OFF_BREAK
import eu.automateeverything.coreplugin.TimedOnOffDeviceConfigurable.Companion.STATE_ON
import eu.automateeverything.coreplugin.TimedOnOffDeviceConfigurable.Companion.STATE_ON_COUNTING
import eu.automateeverything.data.automation.NextStatesDto
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.configurable.Duration
import eu.automateeverything.domain.configurable.StateDeviceConfigurable.Companion.STATE_UNKNOWN
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.Relay
import java.lang.Exception
import java.math.BigDecimal
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
    private val readOnly: Boolean
) : StateDeviceAutomationUnitBase(stateChangeReporter, instanceDto, name, states, false) {

    private var onSince = 0L
    private var offSince = 0L

    @Throws(Exception::class)
    override fun applyNewState(state: String) {
        if (currentState.id == STATE_ON || currentState.id == STATE_ON_COUNTING) {
            changeRelayStateIfNeeded(controlPort, Relay.ON)
            onSince = Calendar.getInstance().timeInMillis
            offSince = 0L
        } else if (currentState.id == STATE_OFF || currentState.id == STATE_OFF_BREAK) {
            changeRelayStateIfNeeded(controlPort, Relay.OFF)
            onSince = 0L
            offSince = Calendar.getInstance().timeInMillis
        }
    }

    @Suppress("SENSELESS_COMPARISON")
    override fun buildNextStates(state: State): NextStatesDto {
        if (readOnly) {
            return NextStatesDto(listOf(), currentState.id, requiresExtendedWidth)
        }

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

        val portReading = (controlPort.requestedValue?.value ?: controlPort.read().value) == BigDecimal.ONE

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

        val newPortReading = (controlPort.requestedValue?.value ?: controlPort.read().value) == BigDecimal.ONE
        if (newPortReading) {
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