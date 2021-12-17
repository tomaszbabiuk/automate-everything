package eu.automateeverything.centralheatingplugin

import eu.automateeverything.centralheatingplugin.CirculationPumpConfigurable.Companion.STATE_OFF
import eu.automateeverything.centralheatingplugin.CirculationPumpConfigurable.Companion.STATE_PUMPING
import eu.automateeverything.centralheatingplugin.CirculationPumpConfigurable.Companion.STATE_WATER_HEATED
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.SensorAutomationUnit
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.configurable.Duration
import eu.automateeverything.domain.configurable.StateDeviceConfigurable.Companion.STATE_UNKNOWN
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.Relay
import eu.automateeverything.domain.hardware.Temperature
import java.math.BigDecimal
import java.util.*

class CirculationPumpAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val pumpPort: OutputPort<Relay>,
    minWorkingTime: Duration,
    private val thermometerId: Long
) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name, ControlType.States, states, false) {

    private lateinit var thermometerUnit: SensorAutomationUnit<Temperature>
    override val usedPortsIds = arrayOf(pumpPort.id)

    override val recalculateOnTimeChange = true
    override val recalculateOnPortUpdate = false
    private var _switchedOffTime = Calendar.getInstance().timeInMillis

    private val _oneMinutesMillis = (90 * 1000).toLong()
    private var lastTemperatureCheck: Long = 0
    private var lastTemperatureMeasured: Temperature? = null

    override fun applyNewState(state: String) {
    }

    private var _lastSwitchingOnTime: Calendar? = null

    private fun isOn(): Boolean {
        return pumpPort.read().value == BigDecimal.ONE
    }

    private val minWorkingTimeCounter = MinWorkingTimeCounter(
        pumpPort.read().value == BigDecimal.ONE,
        minWorkingTime.milliseconds
    )

    override fun calculateInternal(now: Calendar) {
        if (thermometerUnit.lastEvaluation.value == null) {
            return
        }

        if (lastTemperatureMeasured == null) {
            lastTemperatureMeasured = thermometerUnit.lastEvaluation.value
        }

        val nowMillis = now.timeInMillis
        if (currentState.id != STATE_UNKNOWN && currentState.id != STATE_OFF) {
            if (nowMillis - _switchedOffTime > _oneMinutesMillis * 3 && !isOn()) {
                lastTemperatureCheck = nowMillis
                lastTemperatureMeasured = thermometerUnit.lastEvaluation.value
                changeState(STATE_PUMPING)
            } else {
                if (nowMillis - lastTemperatureCheck > _oneMinutesMillis) {
                    lastTemperatureCheck = nowMillis
                    val currentTemperature = thermometerUnit.lastEvaluation.value
                    val temperatureDelta = currentTemperature!!.value.subtract(lastTemperatureMeasured!!.value)
                    val keepPumping = temperatureDelta > BigDecimal.ONE
                    if (keepPumping) {
                        if (!isOn()) {
                            _lastSwitchingOnTime = now
                            changeState(STATE_PUMPING)

                        }
                    } else {
                        if (isOn()) {
                            _switchedOffTime = nowMillis
                            changeState(STATE_WATER_HEATED)
                        }
                    }
                    lastTemperatureMeasured = currentTemperature
                }
            }
        } else {
            changeState(STATE_OFF)
        }

        minWorkingTimeCounter.signal(pumpPort.read().value == BigDecimal.ONE)

        val enablePump = currentState.id == STATE_PUMPING
        if (enablePump) {
            pumpPort.write(Relay.ON)
        } else if (minWorkingTimeCounter.isExceeded) {
            pumpPort.write(Relay.OFF)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun bind(automationUnitsCache: HashMap<Long, Pair<InstanceDto, AutomationUnit<*>>>) {
        thermometerUnit = automationUnitsCache[thermometerId]!!.second as SensorAutomationUnit<Temperature>
    }
}
