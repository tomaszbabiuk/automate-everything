package eu.automateeverything.scenes

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import java.util.*

class SceneAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instanceDto: InstanceDto,
    name: String,
    states: Map<String, State>,
) : StateDeviceAutomationUnitBase(stateChangeReporter, instanceDto, name, states, false) {

    @Throws(Exception::class)
    override fun applyNewState(state: String) {
    }

    override val usedPortsIds: Array<String>
        get() = arrayOf()

    override fun calculateInternal(now: Calendar) {
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = false
}