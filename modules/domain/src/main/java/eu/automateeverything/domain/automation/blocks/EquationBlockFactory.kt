package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.*
import eu.automateeverything.domain.hardware.PortValue

open class EquationBlockFactory<T: PortValue>(
    private val valueType: Class<T>,
    override val category: BlockCategory) : ValueBlockFactory {

    override val type: String = "${valueType.simpleName.lowercase()}_equation"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "%1 %2 %3 %4",
                  "args0": [
                    {
                      "type": "input_value",
                      "name": "LEFT",
                      "check": "${valueType.simpleName}"
                    },
                    {
                      "type": "field_dropdown",
                      "name": "OPERATOR",
                      "options": [
                        [
                          "+",
                          "PLUS"
                        ],
                        [
                          "-",
                          "MINUS"
                        ]
                      ]
                    },
                    {
                      "type": "input_dummy"
                    },
                    {
                      "type": "field_number",
                      "name": "RIGHT",
                      "value": 0,
                      "min": 0,
                      "precision": 0.01
                    }
                  ],
                  "inputsInline": true,
                  "output": "${valueType.simpleName}",
                  "colour": ${category.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): EquationAutomationNode {
        if (block.fields == null || block.fields.size != 2) {
            throw MalformedBlockException(block.type, "should have exactly two FIELDS defined")
        }

        val operatorValue = block.fields.find { it.name == "OPERATOR" }
            ?: throw MalformedBlockException(block.type, "OPERATOR field not found")

        val operator = MathOperator.fromString(operatorValue.value!!)

        val rightValue = block.fields.find { it.name == "RIGHT" }
            ?: throw MalformedBlockException(block.type, "RIGHT value not found")
        val right = rightValue.value!!.toDouble()

        val leftValue = block.values?.find { it.name == "LEFT" }

        var leftNode: ValueNode? = null
        if (leftValue?.block != null) {
            leftNode = transformer.transformValue(leftValue.block, context)
        }

        return EquationAutomationNode(valueType, leftNode, operator, right)
    }
}