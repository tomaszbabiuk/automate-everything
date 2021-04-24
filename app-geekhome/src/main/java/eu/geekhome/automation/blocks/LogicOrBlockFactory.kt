package eu.geekhome.automation.blocks

import eu.geekhome.R
import eu.geekhome.automation.*
import eu.geekhome.rest.RawJson
import eu.geekhome.services.localization.Resource

class LogicOrBlockFactory(private val color: Int) : EvaluatorBlockFactory {

    override val category: Resource = R.category_logic

    override val type: String = "logic_or"

    override fun match(type: String) : Boolean {
        return type == this.type
    }

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
                  "colour": $color,
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