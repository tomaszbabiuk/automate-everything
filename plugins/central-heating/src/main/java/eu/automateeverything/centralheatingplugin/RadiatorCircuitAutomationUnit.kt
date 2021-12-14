package eu.automateeverything.centralheatingplugin

import eu.automateeverything.centralheatingplugin.RadiatorCircuitConfigurable.Companion.STATE_CLOSED
import eu.automateeverything.centralheatingplugin.RadiatorCircuitConfigurable.Companion.STATE_FORCED_CLOSE
import eu.automateeverything.centralheatingplugin.RadiatorCircuitConfigurable.Companion.STATE_OPEN
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
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
    private val inactiveState: InactiveState,
    ) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name, ControlType.States, states, false) {

    private var openingLevel: Long = if (inactiveState == InactiveState.NO) activationTime.milliseconds else 0

    private var counter: Long = Calendar.getInstance().timeInMillis
    override val usedPortsIds = arrayOf(actuatorPort.id)
    var centralHeatingEnabled = false
    var active = false
        private set

    override val recalculateOnTimeChange = true
    override val recalculateOnPortUpdate = true

    override fun applyNewState(state: String) {
        when (state) {
            STATE_OPEN -> {
                if (inactiveState == InactiveState.NC) {
                    actuatorPort.write(Relay(BigDecimal.ONE))
                } else {
                    actuatorPort.write(Relay(BigDecimal.ZERO))
                }
            }

            STATE_CLOSED -> {
                if (inactiveState == InactiveState.NO) {
                    actuatorPort.write(Relay(BigDecimal.ONE))
                } else {
                    actuatorPort.write(Relay(BigDecimal.ZERO))
                }
            }

            STATE_FORCED_CLOSE -> {
                actuatorPort.write(Relay(BigDecimal.ZERO))
            }
        }
    }

    override fun calculateInternal(now: Calendar) {
        forceToActiveIfNeeded()
        calculateOpening(now)
    }

    @Throws(Exception::class)
    private fun forceToActiveIfNeeded() {
        val forceDisable: Boolean = currentState.id == STATE_FORCED_CLOSE
        if (forceDisable) {
            active = false
        } else {
            val forceEnable: Boolean = currentState.id == STATE_FORCED_CLOSE
            val roomRequiresHeating = centralHeatingEnabled && calculateActivity()
            active = forceEnable || roomRequiresHeating
        }
        if (active) {
            changeState(STATE_OPEN)
        }
    }

    private fun calculateActivity(): Boolean {
        //TODO("Not yet implemented")
        return true
    }



    private fun calculateOpening(now: Calendar) {
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
    }

    fun calculateOpeningLevel(): Int {
        val valveOpening = (openingLevel.toDouble() / activationTime.milliseconds * 100).roundToInt()
        modifyNote("valveOpening", Resource.createUniResource(valveOpening.toString()))
        return valveOpening
    }



    fun needsPower(): Boolean {
        val ncActuatorNeedsPower =
            active && (currentState.id == STATE_OPEN) && inactiveState == InactiveState.NC
        val noActuatorNeedsPower =
            active && (currentState.id == STATE_CLOSED) && inactiveState == InactiveState.NO
        return noActuatorNeedsPower || ncActuatorNeedsPower
    }
}