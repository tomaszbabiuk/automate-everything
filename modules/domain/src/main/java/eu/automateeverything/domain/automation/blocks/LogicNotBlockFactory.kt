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
                          "check": "Boolean"
                        }
                      ],
                      "inputsInline": true,
                      "output": "Boolean",
                      "colour": ${category.color},
                      "tooltip": "",
                      "helpUrl": ""
                    }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: IStatementNode?, context: AutomationContext, transformer: IBlocklyTransformer): IEvaluatorNode {
        if (block.values == null || block.values.size != 1) {
            throw MalformedBlockException(block.type, "should have exactly one <VALUE> defined")
        }

        var nodeToNegate: IEvaluatorNode? = null
        val firstValue = block.values.find { it.name == "NOT" }
        if (firstValue == null) {
            throw MalformedBlockException(block.type, "should have <value name=\"NOT\"> defined")
        } else if (firstValue.block != null) {
            nodeToNegate = transformer.transformEvaluator(firstValue.block, context)
        }

        return NotAutomationNode(nodeToNegate)
    }
}