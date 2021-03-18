package eu.geekhome.automation.blocks

import eu.geekhome.automation.*
import eu.geekhome.rest.RawJson
import eu.geekhome.services.hardware.Temperature
import eu.geekhome.services.localization.Resource

class TemperatureEquationBlockFactory : ValueBlockFactory<Temperature> {

    override val category: Resource = R.category_temperature

    override val type: String = "temperature_equation"

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
                      "check": "Temperature"
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
                  "output": "Temperature",
                  "colour": 0,
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: IStatementNode?, context: AutomationContext, transformer: IBlocklyTransformer): TemperatureEquationAutomationNode {
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

        var leftNode: IValueNode<Temperature>? = null
        if (leftValue?.block != null) {
            leftNode = transformer.transformValue(leftValue.block, context)
        }

        return TemperatureEquationAutomationNode(leftNode, operator, right)
    }
}