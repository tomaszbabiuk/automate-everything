package eu.geekhome.automation.blocks

import eu.geekhome.automation.*
import eu.geekhome.rest.RawJson
import eu.geekhome.services.hardware.Temperature
import eu.geekhome.services.localization.Resource

class TemperatureComparisonBlockFactory : EvaluatorBlockFactory {

    override val category: Resource = R.category_temperature

    override val type: String = "temperature_comparison"

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
                          ">",
                          "GREATER"
                        ],
                        [
                          "<",
                          "LESSER"
                        ],
                        [
                          "=",
                          "EQUALS"
                        ],
                        [
                          ">=",
                          "GREATEROREQUAL"
                        ],
                        [
                          "<=",
                          "LESSEROREQUAL"
                        ],
                        [
                          "!=",
                          "NOTEQUALS"
                        ]
                      ]
                    },
                    {
                      "type": "input_dummy"
                    },
                    {
                      "type": "input_value",
                      "name": "RIGHT",
                      "check": "Temperature"
                    }
                  ],
                  "inputsInline": true,
                  "output": "Boolean",
                  "colour": 0,
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: IStatementNode?, context: AutomationContext, transformer: IBlocklyTransformer): IEvaluatorNode {
//        if (block.values == null || block.values.size != 2) {
//            throw MalformedBlockException(block.type, "should have exactly two <VALUE> defined")
//        }

        if (block.fields == null) {
            throw MalformedBlockException(block.type, "should have one field defined")
        }

        if (block.fields.size != 1) {
            throw MalformedBlockException(block.type, "should have one <OPERATOR> field defined")
        }

        val operator = ComparisonOperator.fromString(block.fields[0].value!!)

        var leftNode: IValueNode<Temperature>? = null
        val leftValue = block.values?.find { it.name == "LEFT" }
        if (leftValue?.block != null) {
            leftNode = transformer.transformValue(leftValue.block, context)
        }

        var rightNode: IValueNode<Temperature>? = null
        val rightValue = block.values?.find { it.name == "RIGHT" }
        if (rightValue?.block != null) {
            rightNode = transformer.transformValue(rightValue.block, context)
        }

        return ComparisonAutomationNode(leftNode, operator, rightNode)
    }
}