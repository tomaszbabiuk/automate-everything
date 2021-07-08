package eu.geekhome.domain.automation.blocks

import eu.geekhome.domain.R
import eu.geekhome.domain.automation.*
import eu.geekhome.domain.localization.Resource

class LogicNotBlockFactory(private val color: Int) : EvaluatorBlockFactory {

    override val category: Resource = R.category_logic

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
                      "colour": $color,
                      "tooltip": "",
                      "helpUrl": ""
                    }
                """.trimIndent()
        }
    }

    override fun match(type: String): Boolean {
        return type == this.type
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