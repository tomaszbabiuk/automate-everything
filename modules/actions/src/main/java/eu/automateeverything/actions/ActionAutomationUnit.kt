package eu.automateeverything.actions

import eu.automateeverything.actions.ActionConfigurableBase.Companion.STATE_CANCELLED
import eu.automateeverything.actions.ActionConfigurableBase.Companion.STATE_EXECUTING
import eu.automateeverything.actions.ActionConfigurableBase.Companion.STATE_FAILURE
import eu.automateeverything.actions.ActionConfigurableBase.Companion.STATE_READY
import eu.automateeverything.actions.ActionConfigurableBase.Companion.STATE_SUCCESS
import eu.automateeverything.data.automation.NextStatesDto
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.LocalizedException
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
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
) : StateDeviceAutomationUnitBase(stateChangeReporter, instanceDto, name, states, false) {

    private var executionScope: CoroutineScope? = null

    @Throws(Exception::class)
    override fun applyNewState(state: String) {
        if (state == STATE_CANCELLED) {
            executionScope?.cancel("Cancelled...")
        }

        if (state == STATE_EXECUTING) {
            executionScope?.cancel("New execution, previous must have been cancelled...")
            executionScope = CoroutineScope(Dispatchers.IO)
            executionScope!!.launch {
                try {
                    val result = executionCode()
                    if (isActive) {
                        modifyNote(EVALUATION_OUTPUT, result.second)
                        if (result.first) {
                            changeState(STATE_SUCCESS)
                        } else {
                            changeState(STATE_FAILURE)
                        }
                    }
                } catch (ex: LocalizedException) {
                    modifyNote(EVALUATION_OUTPUT, ex.localizedMessage)
                    changeState(STATE_FAILURE)
                } catch (ex: Exception) {
                    modifyNote(EVALUATION_OUTPUT, Resource.createUniResource(ex.message!!))
                    changeState(STATE_FAILURE)
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