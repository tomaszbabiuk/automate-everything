package eu.geekhome.domain.automation.blocks

import eu.geekhome.data.automation.State
import eu.geekhome.data.blocks.RawJson
import eu.geekhome.domain.R
import eu.geekhome.domain.automation.*
import eu.geekhome.domain.configurable.StateDeviceConfigurable
import eu.geekhome.data.localization.Resource

class ChangeStateBlockFactory(private val state: State) : StatementBlockFactory {

    override val category: Resource = R.category_this_device

    override val type: String = "change_state_${state.id}"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                   { "type":  "$type",
                     "colour": 230,
                     "tooltip": null,
                     "helpUrl": null,
                     "message0": "${state.name.getValue(it)}",
                     "previousStatement": null,
                     "nextStatement": null }
                """.trimIndent()
        }
    }

    override fun match(type: String): Boolean {
        return type.startsWith(this.type)
    }

    override fun transform(
        block: Block,
        next: IStatementNode?,
        context: AutomationContext,
        transformer: IBlocklyTransformer
    ): IStatementNode {
        if (context.thisDevice is StateDeviceConfigurable) {
            val evaluator = context.automationUnitsCache[context.instanceDto.id]
            if (evaluator is StateDeviceAutomationUnit) {
                return ChangeStateAutomationNode(state.id, evaluator, next)
            } else {
                throw MalformedBlockException(block.type, "should point only to a state device")
            }
        }

        throw MalformedBlockException(block.type, "it's impossible to connect this block with correct ${StateDeviceConfigurable::class.java}")
    }
}