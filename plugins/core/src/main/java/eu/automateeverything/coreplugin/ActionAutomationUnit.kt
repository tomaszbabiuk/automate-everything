package eu.automateeverything.coreplugin

import eu.automateeverything.coreplugin.ActionConfigurableBase.Companion.STATE_EXECUTED
import eu.automateeverything.coreplugin.ActionConfigurableBase.Companion.STATE_READY
import eu.geekhome.data.automation.NextStatesDto
import eu.geekhome.data.automation.State
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.automation.StateChangeReporter
import eu.geekhome.domain.automation.StateDeviceAutomationUnit
import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

class ActionAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instanceDto: InstanceDto,
    name: String,
    private val resetRequired: Boolean,
    states: Map<String, State>,
    private val executionCode: () -> String
) : StateDeviceAutomationUnit(stateChangeReporter, instanceDto, name, states, false) {

    @Throws(Exception::class)
    override fun applyNewState(state: String) {
        if (state == STATE_EXECUTED) {
            val result = executionCode()
            modifyNote(EVALUATION_OUTPUT, Resource.createUniResource(result))
            if (!resetRequired) {
                changeState(STATE_READY)
            }
        }

        if (state == STATE_READY) {
            removeNote(EVALUATION_OUTPUT)
        }
    }


    override fun buildNextStates(state: State): NextStatesDto {
        return when (state.id) {
            STATE_READY -> {
                onlyOneState(STATE_EXECUTED)
            }
            else -> {
                onlyOneState(STATE_READY)
            }
        }
    }

    override val usedPortsIds: Array<String> = arrayOf()

    override fun calculateInternal(now: Calendar) {
    }

    init {
        changeState(STATE_READY)
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = false

    companion object {
        const val EVALUATION_OUTPUT = "output"
    }
}