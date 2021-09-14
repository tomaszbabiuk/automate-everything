package eu.automateeverything.coreplugin

import eu.automateeverything.coreplugin.ActionConfigurableBase.Companion.STATE_CANCELLED
import eu.automateeverything.coreplugin.ActionConfigurableBase.Companion.STATE_EXECUTING
import eu.automateeverything.coreplugin.ActionConfigurableBase.Companion.STATE_FAILURE
import eu.automateeverything.coreplugin.ActionConfigurableBase.Companion.STATE_READY
import eu.automateeverything.coreplugin.ActionConfigurableBase.Companion.STATE_SUCCESS
import eu.geekhome.data.automation.NextStatesDto
import eu.geekhome.data.automation.State
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.automation.StateChangeReporter
import eu.geekhome.domain.automation.StateDeviceAutomationUnit
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

class ActionAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instanceDto: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val executionCode: () -> Pair<Boolean,Resource>
) : StateDeviceAutomationUnit(stateChangeReporter, instanceDto, name, states, false) {

    private var executionScope: CoroutineScope? = null

    @Throws(Exception::class)
    override fun applyNewState(state: String) {
        if (state == STATE_CANCELLED) {
            executionScope?.cancel("Restarting...")
        }

        if (state == STATE_EXECUTING) {
            executionScope?.cancel("Restarting...")
            executionScope = CoroutineScope(Dispatchers.IO)
            executionScope!!.launch {
                val result = executionCode()
                if (isActive) {
                    modifyNote(EVALUATION_OUTPUT, result.second)
                    if (result.first) {
                        changeState(STATE_SUCCESS)
                    } else {
                        changeState(STATE_FAILURE)
                    }
                }
            }
        }

        if (state == STATE_READY) {
            removeNote(EVALUATION_OUTPUT)
        }
    }


    override fun buildNextStates(state: State): NextStatesDto {
        return when (state.id) {
            STATE_EXECUTING -> {
                onlyOneState(STATE_CANCELLED)
            }
            STATE_READY -> {
                onlyOneState(STATE_EXECUTING)
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