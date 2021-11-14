package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.R
import eu.automateeverything.domain.automation.*

class LogicNotBlockFactory : EvaluatorBlockFactory {

    override val category = CommonBlockCategories.Logic

    override val type: String = "logic_not"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                    {
                      "type": "$type",
                      "message0": "${R.block_label_not.getValue(it)}",
                      "args0": [
                        {
                          "type": "input_dummy"
                        },
                        {
                          "type": "input_value",
                          "name": "NOT",
                          "check": "${Boolean::class.java.simpleName}"
                        }
                      ],
                      "inputsInline": true,
                      "output": "${Boolean::class.java.simpleName}",
                      "colour": ${category.color},
                      "tooltip": "",
                      "helpUrl": ""
                    }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): EvaluatorNode {
        if (block.values == null || block.values.size != 1) {
            throw MalformedBlockException(block.type, "should have exactly one <VALUE> defined")
        }

        var nodeToNegate: EvaluatorNode? = null
        val firstValue = block.values.find { it.name == "NOT" }
        if (firstValue == null) {
            throw MalformedBlockException(block.type, "should have <value name=\"NOT\"> defined")
        } else if (firstValue.block != null) {
            nodeToNegate = transformer.transformEvaluator(firstValue.block, context)
        }

        return NotAutomationNode(nodeToNegate)
    }
}