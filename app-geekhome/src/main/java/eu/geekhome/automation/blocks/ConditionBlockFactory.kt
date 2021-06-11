package eu.geekhome.automation.blocks

import eu.geekhome.R
import eu.geekhome.automation.*
import eu.geekhome.rest.RawJson
import eu.geekhome.domain.configurable.ConditionConfigurable
import eu.geekhome.domain.localization.Resource

class ConditionBlockFactory(private val conditionId: Long, private val label: Resource) : EvaluatorBlockFactory {

    override val category: Resource = R.category_triggers_conditions

    override val type: String = "condition_$conditionId"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                   { "type":  "$type",
                     "colour": 120,
                     "tooltip": null,
                     "helpUrl": null,
                     "message0": "${label.getValue(it)}",
                     "output": null }
                """.trimIndent()
        }
    }

    override fun match(type: String): Boolean {
        return type.startsWith("condition_")
    }

    override fun transform(
        block: Block,
        next: IStatementNode?,
        context: AutomationContext,
        transformer: IBlocklyTransformer
    ): IEvaluatorNode {
        val evaluator = context.evaluationUnitsCache[this.conditionId]

        if (evaluator != null) {
            return ConditionAutomationNode(evaluator)
        }

        throw MalformedBlockException(block.type,
            "it's impossible to connect this block with correct ${ConditionConfigurable::class.java}")
    }
}