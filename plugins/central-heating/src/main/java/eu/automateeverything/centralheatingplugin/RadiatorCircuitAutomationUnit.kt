package eu.automateeverything.centralheatingplugin

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import java.util.*

class RadiatorCircuitAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name, ControlType.States, states, false) {

    override fun applyNewState(state: String) {
        TODO("Not yet implemented")
    }

    override val usedPortsIds: Array<String>
        get() = TODO("Not yet implemented")
    override val recalculateOnTimeChange: Boolean
        get() = TODO("Not yet implemented")
    override val recalculateOnPortUpdate: Boolean
        get() = TODO("Not yet implemented")

    override fun calculateInternal(now: Calendar) {
        TODO("Not yet implemented")
    }

    fun calculateOpeningLevel(): Int {
        TODO("Not yet implemented")
    }

    fun isActive(): Boolean {
        TODO("Not yet implemented")
    }

    fun needsPower(): Boolean {
        TODO("Not yet implemented")
    }

    fun setCentralHeatingEnabled(heatingEnabled: Any) {
        TODO("Not yet implemented")
    }
}