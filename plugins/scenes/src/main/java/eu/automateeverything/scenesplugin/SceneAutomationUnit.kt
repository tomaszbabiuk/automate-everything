package eu.automateeverything.scenesplugin

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.scenesplugin.SceneConfigurable.Companion.STATE_INACTIVE
import java.util.*

class SceneAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    automationOnly: Boolean,
    states: Map<String, State>,
) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name,
        if (automationOnly) { ControlType.NA } else { ControlType.States },
        states, false) {

    init {
        changeState(STATE_INACTIVE)
    }

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