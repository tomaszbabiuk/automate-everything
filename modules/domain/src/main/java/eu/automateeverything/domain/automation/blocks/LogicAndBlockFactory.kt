package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.R
import eu.automateeverything.domain.automation.*

class LogicAndBlockFactory : EvaluatorBlockFactory {

    override val category = CommonBlockCategories.Logic

    override val type: String = "logic_and"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                    {
                      "type": "$type",
                      "message0": "${R.block_label_and.getValue(it)}",
                      "args0": [
                        {
                          "type": "input_value",
                          "name": "FIRST",
                          "check": "${Boolean::class.java.simpleName}"
                        },
                        {
                          "type": "input_dummy"
                        },
                        {
                          "type": "input_value",
                          "name": "SECOND",
                          "check": "${Boolean::class.java.simpleName}"
                        }
                      ],
                      "inputsInline": true,
                      "output": null,
                      "colour": ${category.color},
                      "tooltip": "",
                      "helpUrl": "http://www.example.com/"
                    }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): EvaluatorNode {
        if (block.values == null || block.values.size != 2) {
            throw MalformedBlockException(block.type, "should have exactly two <VALUE> defined")
        }

        var firstNode: EvaluatorNode? = null
        val firstValue = block.values.find { it.name == "FIRST" }
        if (firstValue == null) {
            throw MalformedBlockException(block.type, "should have <value name=\"FIRST\"> defined")
        } else if (firstValue.block != null) {
            firstNode = transformer.transformEvaluator(firstValue.block, context)
        }

        var secondNode: EvaluatorNode? = null
        val secondValue = block.values.find { it.name == "SECOND" }
        if (secondValue == null) {
            throw MalformedBlockException(block.type, "should have <value name=\"SECOND\"> defined")
        } else if (secondValue.block != null) {
            secondNode = transformer.transformEvaluator(secondValue.block, context)
        }

        return AndAutomationNode(firstNode, secondNode)
    }
}