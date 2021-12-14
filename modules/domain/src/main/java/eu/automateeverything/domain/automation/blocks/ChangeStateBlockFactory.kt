package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.*
import eu.automateeverything.domain.configurable.StateDeviceConfigurable

class ChangeStateBlockFactory(private val state: State) : StatementBlockFactory {

    override val category = CommonBlockCategories.ThisObject

    override val type: String = "change_state_${state.id}"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                   { "type":  "$type",
                     "colour": ${category.color},
                     "tooltip": null,
                     "helpUrl": null,
                     "message0": "${state.action!!.getValue(it)}",
                     "previousStatement": null,
                     "nextStatement": null }
                """.trimIndent()
        }
    }

    override fun transform(
        block: Block,
        next: StatementNode?,
        context: AutomationContext,
        transformer: BlocklyTransformer
    ): StatementNode {
        if (context.thisDevice is StateDeviceConfigurable) {
            val evaluator = context.automationUnitsCache[context.instance.id]
            if (evaluator is StateDeviceAutomationUnitBase) {
                return ChangeStateAutomationNode(state.id, evaluator, next)
            } else {
                throw MalformedBlockException(block.type, "should point only to a state device")
            }
        }

        throw MalformedBlockException(block.type, "it's impossible to connect this block with correct ${StateDeviceConfigurable::class.java}")
    }
}