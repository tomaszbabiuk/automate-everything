package eu.geekhome.domain.automation.blocks

import eu.geekhome.data.blocks.RawJson
import eu.geekhome.domain.R
import eu.geekhome.domain.automation.*

class LogicOrBlockFactory : EvaluatorBlockFactory {

    override val category = CategoryConstants.Logic

    override val type: String = "logic_or"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "${R.block_label_or.getValue(it)}",
                  "args0": [
                    {
                      "type": "input_value",
                      "name": "FIRST",
                      "check": "Boolean"
                    },
                    {
                      "type": "input_dummy"
                    },
                    {
                      "type": "input_value",
                      "name": "SECOND",
                      "check": "Boolean"
                    }
                  ],
                  "inputsInline": false,
                  "output": "Boolean",
                  "colour": ${category.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: IStatementNode?, context: AutomationContext, transformer: IBlocklyTransformer): IEvaluatorNode {
        if (block.values == null || block.values.size != 2) {
            throw MalformedBlockException(block.type, "should have exactly two <VALUE> defined")
        }

        var firstNode: IEvaluatorNode? = null
        val firstValue = block.values.find { it.name == "FIRST" }
        if (firstValue == null) {
            throw MalformedBlockException(block.type, "should have <value name=\"FIRST\"> defined")
        } else if (firstValue.block != null) {
            firstNode = transformer.transformEvaluator(firstValue.block, context)
        }

        var secondNode: IEvaluatorNode? = null
        val secondValue = block.values.find { it.name == "SECOND" }
        if (secondValue == null) {
            throw MalformedBlockException(block.type, "should have <value name=\"SECOND\"> defined")
        } else if (secondValue.block != null) {
            secondNode = transformer.transformEvaluator(secondValue.block, context)
        }

        return AndAutomationNode(firstNode, secondNode)
    }
}