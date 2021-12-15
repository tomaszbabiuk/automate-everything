package eu.automateeverything.centralheatingplugin

import eu.automateeverything.centralheatingplugin.RadiatorCircuitConfigurable.Companion.NOTE_VALVE_OPENING
import eu.automateeverything.centralheatingplugin.RadiatorCircuitConfigurable.Companion.STATE_DISABLED
import eu.automateeverything.centralheatingplugin.RadiatorCircuitConfigurable.Companion.STATE_ENABLED
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.configurable.Duration
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.Relay
import java.math.BigDecimal
import java.util.*
import kotlin.math.roundToInt

class RadiatorCircuitAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val actuatorPort: OutputPort<Relay>,
    private val activationTime: Duration,
    val inactiveState: InactiveState,
    ) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name, ControlType.States, states, false) {

    private var openingLevel: Long = if (inactiveState == InactiveState.NO) activationTime.milliseconds else 0

    private var counter: Long = Calendar.getInstance().timeInMillis
    override val usedPortsIds = arrayOf(actuatorPort.id)
//    var anyOtherLineIsActive = false

    fun isActive() = currentState.id == STATE_ENABLED

    override val recalculateOnTimeChange = true
    override val recalculateOnPortUpdate = true

    init {
        changeState(STATE_DISABLED)
    }

    override fun applyNewState(state: String) {
//        when (state) {
//            STATE_ENABLED -> {
//                if (calculateOpeningLevel() != 100) {
//                    if (inactiveState == InactiveState.NC) {
//                        actuatorPort.write(Relay.ON)
//                    } else {
//                        actuatorPort.write(Relay.OFF)
//                    }
//                }
//            }
//
//            STATE_DISABLE -> {
//                if (inactiveState == InactiveState.NO) {
//                    actuatorPort.write(Relay.ON)
//                } else {
//                    actuatorPort.write(Relay.OFF)
//                }
//            }
//
//            else -> {
//                actuatorPort.write(Relay.OFF)
//            }
//        }

//        if (getStateId().equals("open")) {
//            if (calculateOpenningLevel() != 100) {
//                if (getDevice().getInactiveState() === InactiveState.NC) {
//                    changeOutputPortStateIfNeeded(_outputPort, true)
//                } else {
//                    changeOutputPortStateIfNeeded(_outputPort, false)
//                }
//            }
//        }
//
//        if (getStateId().equals("closed")) {
//            if (getDevice().getInactiveState() === InactiveState.NO) {
//                changeOutputPortStateIfNeeded(_outputPort, true)
//            } else {
//                changeOutputPortStateIfNeeded(_outputPort, false)
//            }
//        }
    }

    override fun calculateInternal(now: Calendar) {
        val ticksDelta: Long = now.timeInMillis - counter
        counter = now.timeInMillis
        if (actuatorPort.read().value == BigDecimal.ONE) {
            if (inactiveState == InactiveState.NO) {
                //slowly close NO actuator when powered
                if (openingLevel > 0) {
                    openingLevel -= ticksDelta
                }
            } else {
                //slowly open NC actuator when powered
                if (openingLevel < activationTime.milliseconds) {
                    openingLevel += ticksDelta
                }
            }
        } else {
            if (inactiveState == InactiveState.NO) {
                //slowly open NO actuator when powered
                if (openingLevel < activationTime.milliseconds) {
                    openingLevel += ticksDelta
                }
            } else {
                //slowly close NC actuator when powered
                if (openingLevel > 0) {
                    openingLevel -= ticksDelta
                }
            }
        }
        if (openingLevel < 0) {
            openingLevel = 0
        }
        if (openingLevel > activationTime.milliseconds) {
            openingLevel = activationTime.milliseconds
        }

//        val ncActuatorShouldBePowered = (inactiveState == InactiveState.NC) && currentState.id == STATE_ENABLED
//        val noActuatorShouldBePowered = (inactiveState == InactiveState.NO) && anyOtherLineIsActive
//        val actuatorShouldBePowered = ncActuatorShouldBePowered || noActuatorShouldBePowered
//        actuatorPort.write(if (actuatorShouldBePowered) Relay.ON else Relay.OFF)
    }

    fun calculateValveLevel(): Int {
        val valveOpening = (openingLevel.toDouble() / activationTime.milliseconds * 100).roundToInt()
        modifyNote(NOTE_VALVE_OPENING, R.note_opening_level(valveOpening))
        return valveOpening
    }

    fun needsPower(): Boolean {
        val ncActuatorNeedsPower =
            isActive() && (currentState.id == STATE_ENABLED) && inactiveState == InactiveState.NC
        val noActuatorNeedsPower =
            isActive() && (currentState.id == STATE_DISABLED) && inactiveState == InactiveState.NO
        return noActuatorNeedsPower || ncActuatorNeedsPower
    }

    fun disableRelay() {
        actuatorPort.write(Relay.OFF)
    }

    fun enableRelay() {
        actuatorPort.write(Relay.ON)
    }
}