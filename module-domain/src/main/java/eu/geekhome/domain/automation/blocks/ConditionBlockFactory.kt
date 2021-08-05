package eu.geekhome.domain.automation.blocks

import eu.geekhome.data.blocks.RawJson
import eu.geekhome.domain.automation.*
import eu.geekhome.domain.configurable.ConditionConfigurable
import eu.geekhome.data.localization.Resource

class ConditionBlockFactory(
    private val conditionId: Long,
    private val label: Resource) : EvaluatorBlockFactory {

    override val category = CategoryConstants.Conditions

    override val type: String = "condition_$conditionId"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                   { "type":  "$type",
                     "colour": ${category.color},
                     "tooltip": null,
                     "helpUrl": null,
                     "message0": "${label.getValue(it)}",
                     "output": null }
                """.trimIndent()
        }
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