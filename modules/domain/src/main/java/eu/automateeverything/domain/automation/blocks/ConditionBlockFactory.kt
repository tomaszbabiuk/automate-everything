package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.*
import eu.automateeverything.domain.configurable.ConditionConfigurable
import eu.automateeverything.data.localization.Resource

class ConditionBlockFactory(
    private val conditionId: Long,
    private val label: Resource) : EvaluatorBlockFactory {

    override val category = CommonBlockCategories.Conditions

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
        next: StatementNode?,
        context: AutomationContext,
        transformer: BlocklyTransformer
    ): EvaluatorNode {
        val evaluator = context.evaluationUnitsCache[this.conditionId]

        if (evaluator != null) {
            return ConditionAutomationNode(evaluator)
        }

        throw MalformedBlockException(block.type,
            "it's impossible to connect this block with correct ${ConditionConfigurable::class.java}")
    }

    override fun dependsOn(): List<Long> {
        return listOf(conditionId)
    }
}