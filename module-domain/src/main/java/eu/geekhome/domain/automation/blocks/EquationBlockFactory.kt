package eu.geekhome.domain.automation.blocks

import eu.geekhome.domain.automation.*
import eu.geekhome.domain.hardware.PortValue
import eu.geekhome.domain.localization.Resource

class EquationBlockFactory<T: PortValue>(
    private val valueType: Class<T>,
    override val category: Resource,
    private val color: Int) : ValueBlockFactory {

    override val type: String = "${valueType.simpleName.lowercase()}_equation"

    override fun match(type: String) : Boolean {
        return type == this.type
    }

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
                  "colour": $color,
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: IStatementNode?, context: AutomationContext, transformer: IBlocklyTransformer): EquationAutomationNode {
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

        var leftNode: IValueNode? = null
        if (leftValue?.block != null) {
            leftNode = transformer.transformValue(leftValue.block, context)
        }

        return EquationAutomationNode(valueType, leftNode, operator, right)
    }
}