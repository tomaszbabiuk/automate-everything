package eu.geekhome.domain.automation.blocks

import eu.geekhome.data.blocks.RawJson
import eu.geekhome.domain.automation.*
import eu.geekhome.domain.hardware.PortValue

open class ComparisonBlockFactory<T: PortValue>(
    private val valueType: Class<T>,
    override val category: BlockCategory) : EvaluatorBlockFactory {

    override val type: String = "${valueType.simpleName.lowercase()}_comparison"

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
                  "colour": ${category.color},
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