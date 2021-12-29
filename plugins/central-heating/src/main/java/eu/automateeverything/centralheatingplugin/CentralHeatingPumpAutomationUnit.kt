package eu.automateeverything.centralheatingplugin


import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.configurable.StateDeviceConfigurable
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.Relay
import java.util.*

class CentralHeatingPumpAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val pumpPort: OutputPort<Relay>?,
    private val transformerPort: OutputPort<Relay>?,
    private val thermalActuatorIds: List<Long>,
) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name, ControlType.States, states, false) {

    override fun applyNewState(state: String) {
    }

    private lateinit var actuatorUnits: List<ThermalActuatorAutomationUnit>

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
        for (actuator in actuatorUnits) {
            val openingLevel: Int = actuator.calculateValveLevel()
            if (actuator.isActive()) {
                isAnyLineActive = true
                if (openingLevel == 100) {
                    isAnyActiveLineOpened = true
                }
            } else {
                if (openingLevel != 0) {
                    isEveryInactiveLineClosed = false
                }
            }
        }

        if (!isAnyLineActive) {
            //disable relays if there's no active lines
            actuatorUnits.forEach { it.disableRelay() }
        } else {
            //control relays
            actuatorUnits.forEach {
                if (it.needsARelay()) {
                    actuatorsNeedPower = true
                    it.enableRelay()
                } else {
                    it.disableRelay()
                }
            }
        }

        val enablePump = heatingEnabled && isAnyLineActive && isAnyActiveLineOpened && isEveryInactiveLineClosed
        val needsRegulation = heatingEnabled && (isAnyLineActive || actuatorsNeedPower)
        val enableTransformer = heatingEnabled && actuatorsNeedPower
        if (needsRegulation && !enablePump) {
            changeState(CentralHeatingPumpConfigurable.STATE_REGULATION)
        } else if (enablePump) {
            changeState(CentralHeatingPumpConfigurable.STATE_PUMPING)
        } else {
            changeState(CentralHeatingPumpConfigurable.STATE_STANDBY)
        }

        transformerPort?.write(if (enableTransformer) Relay.ON else Relay.OFF)

        if (pumpPort != null) {
            if (enablePump) {
                pumpPort.write(Relay.ON)
            } else {
                pumpPort.write(Relay.OFF)
            }
        }
    }

    override fun bind(automationUnitsCache: HashMap<Long, Pair<InstanceDto, AutomationUnit<*>>>) {
        actuatorUnits = thermalActuatorIds.map { automationUnitsCache[it]!!.second as ThermalActuatorAutomationUnit }
    }
}