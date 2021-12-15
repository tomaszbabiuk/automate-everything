package eu.automateeverything.centralheatingplugin


import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.configurable.Duration
import eu.automateeverything.domain.configurable.StateDeviceConfigurable
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.Relay
import java.math.BigDecimal
import java.util.*

class HeatingManifoldAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val pumpPort: OutputPort<Relay>?,
    minimumPumpWorkingTime: Duration,
    private val transformerPort: OutputPort<Relay>?,
    private val circuitIds: List<Long>,
) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name, ControlType.States, states, false) {

    override fun applyNewState(state: String) {
        when (state) {
            HeatingManifoldConfigurable.STATE_REGULATION -> {
                transformerPort?.write(Relay(BigDecimal.ONE))
                pumpPort?.write(Relay(BigDecimal.ZERO))
            }
            HeatingManifoldConfigurable.STATE_PUMPING -> {
                transformerPort?.write(Relay(BigDecimal.ONE))
                pumpPort?.write(Relay(BigDecimal.ONE))
            }
            else -> {
                transformerPort?.write(Relay(BigDecimal.ZERO))
                pumpPort?.write(Relay(BigDecimal.ZERO))
            }
        }
    }

    private val minWorkingTimeCounter = if (pumpPort == null) null else MinWorkingTimeCounter(
        pumpPort.read().value == BigDecimal.ONE,
        minimumPumpWorkingTime.milliseconds
    )

    private lateinit var circuitUnits: List<RadiatorCircuitAutomationUnit>

    override val usedPortsIds: Array<String>
        get() = listOfNotNull(pumpPort?.id, transformerPort?.id).toTypedArray()

    override val recalculateOnTimeChange = true
    override val recalculateOnPortUpdate = true

    override fun calculateInternal(now: Calendar) {
        val heatingEnabled: Boolean = currentState.id != StateDeviceConfigurable.STATE_UNKNOWN
        var isAnyLineActive = false
        var isAnyActiveLineOpened = false
        var isEveryInactiveLineClosed = true
        var actuatorsNeedPower = false

        //check if any line is enabled/active
        for (circuit in circuitUnits) {
            val openingLevel: Int = circuit.calculateValveLevel()
            if (circuit.isActive()) {
                isAnyLineActive = true
                if (openingLevel == 100) {
                    isAnyActiveLineOpened = true
                }
            } else {
                if (openingLevel != 0) {
                    isEveryInactiveLineClosed = false
                }
            }
            if (circuit.needsPower()) {
                actuatorsNeedPower = true
            }
        }

        if (!isAnyLineActive) {
            //disable relays if there's no active lines
            circuitUnits.forEach { it.disableRelay() }
        } else {
            circuitUnits.forEach {
                if (it.inactiveState == InactiveState.NC) {
                    if (it.isActive()) {
                        it.enableRelay()
                    } else {
                        it.disableRelay()
                    }
                } else { //NO
                    if (it.isActive()) {
                        it.disableRelay()
                    } else {
                        it.enableRelay()
                    }
                }
            }
        }

        val enablePump = heatingEnabled && isAnyLineActive && isAnyActiveLineOpened && isEveryInactiveLineClosed
        val enableTransformer = heatingEnabled && (isAnyLineActive || actuatorsNeedPower)
        if (enableTransformer && !enablePump) {
            changeState(HeatingManifoldConfigurable.STATE_REGULATION)
        } else if (enablePump) {
            changeState(HeatingManifoldConfigurable.STATE_PUMPING)
        } else {
            changeState(HeatingManifoldConfigurable.STATE_STANDBY)
        }

        if (pumpPort != null) {
            minWorkingTimeCounter?.signal(pumpPort.read().value == BigDecimal.ONE)
        }
    }

    override fun bind(automationUnitsCache: HashMap<Long, Pair<InstanceDto, AutomationUnit<*>>>) {
        circuitUnits = circuitIds.map { automationUnitsCache[it]!!.second as RadiatorCircuitAutomationUnit }
    }
}
