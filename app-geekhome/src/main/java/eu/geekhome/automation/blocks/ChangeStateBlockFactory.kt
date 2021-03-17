package eu.geekhome.automation.blocks

import eu.geekhome.automation.*
import eu.geekhome.rest.RawJson
import eu.geekhome.services.automation.State
import eu.geekhome.services.automation.StateDeviceAutomationUnit
import eu.geekhome.services.configurable.StateDeviceConfigurable
import eu.geekhome.services.localization.Resource

class ChangeStateBlockFactory(private val state: State) : StatementBlockFactory {

    override val category: Resource = R.category_name_this_device

    override val type: String = "change_state_${state.name.id}"

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
            var evaluator = context.automationUnitsCache[context.instanceDto.id]
            if (evaluator is AutomationUnitWrapper) {
                evaluator = evaluator.wrapped
            }
            if (evaluator is StateDeviceAutomationUnit) {
                return ChangeStateAutomationNode(state.name.id, evaluator, next)
            } else {
                throw MalformedBlockException(block.type, "should point only to a state device")
            }
        }

        throw MalformedBlockException(block.type, "it's impossible to connect this block with correct ${StateDeviceConfigurable::class.java}")
    }
}