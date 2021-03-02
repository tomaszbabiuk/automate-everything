package eu.geekhome.automation.blocks

import eu.geekhome.automation.*
import eu.geekhome.rest.RawJson
import eu.geekhome.services.localization.Resource

class LogicNotBlockFactory : ValueBlockFactory {

    override val category: Resource = R.category_name_logic

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
                      "colour": 120,
                      "tooltip": "",
                      "helpUrl": ""
                    }
                """.trimIndent()
        }
    }

    override fun match(type: String): Boolean {
        return type == this.type
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: IBlocklyTransformer): ValueNode {
        if (block.values == null || block.values.size != 1) {
            throw MalformedBlockException(block.type, "should have exactly one <VALUE> defined")
        }

        var nodeToNegate: ValueNode? = null
        val firstValue = block.values.find { it.name == "NOT" }
        if (firstValue == null) {
            throw MalformedBlockException(block.type, "should have <value name=\"NOT\"> defined")
        } else if (firstValue.block != null) {
            nodeToNegate = transformer.transformValue(firstValue.block, context)
        }

        return NotAutomationNode(nodeToNegate)
    }
}