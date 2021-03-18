package eu.geekhome.automation.blocks

import eu.geekhome.automation.*
import eu.geekhome.rest.RawJson
import eu.geekhome.services.hardware.PortValue
import eu.geekhome.services.localization.Resource

class ComparisonBlockFactory<T: PortValue>(
    private val valueType: Class<T>,
    override val category: Resource,
    private val color: Int) : EvaluatorBlockFactory {

    override val type: String = "${valueType.simpleName.toLowerCase()}_comparison"

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
                      "check": "${valueType.simpleName}"
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

    override fun transform(block: Block, next: IStatementNode?, context: AutomationContext, transformer: IBlocklyTransformer): IEvaluatorNode {
        if (block.fields == null) {
            throw MalformedBlockException(block.type, "should have one field defined")
        }

        if (block.fields.size != 1) {
            throw MalformedBlockException(block.type, "should have one <OPERATOR> field defined")
        }

        val operator = ComparisonOperator.fromString(block.fields[0].value!!)

        var leftNode: IValueNode? = null
        val leftValue = block.values?.find { it.name == "LEFT" }
        if (leftValue?.block != null) {
            leftNode = transformer.transformValue(leftValue.block, context)
        }

        var rightNode: IValueNode? = null
        val rightValue = block.values?.find { it.name == "RIGHT" }
        if (rightValue?.block != null) {
            rightNode = transformer.transformValue(rightValue.block, context)
        }

        return ComparisonAutomationNode(leftNode, operator, rightNode)
    }
}