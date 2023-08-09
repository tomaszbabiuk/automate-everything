/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.*
import eu.automateeverything.data.hardware.PortValue

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
                  "output": "${Boolean::class.java.simpleName}",
                  "colour": ${category.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(
        block: Block,
        next: StatementNode?,
        context: AutomationContext,
        transformer: BlocklyTransformer,
        order: Int
    ): EvaluatorNode {
        if (block.fields == null) {
            throw MalformedBlockException(block.type, "should have one field defined")
        }

        if (block.fields.size != 1) {
            throw MalformedBlockException(block.type, "should have one <OPERATOR> field defined")
        }

        val operator = ComparisonOperator.fromString(block.fields[0].value!!)

        var leftNode: ValueNode? = null
        val leftValue = block.values?.find { it.name == "LEFT" }
        if (leftValue?.block != null) {
            leftNode = transformer.transformValue(leftValue.block, context)
        }

        var rightNode: ValueNode? = null
        val rightValue = block.values?.find { it.name == "RIGHT" }
        if (rightValue?.block != null) {
            rightNode = transformer.transformValue(rightValue.block, context)
        }

        return ComparisonAutomationNode(leftNode, operator, rightNode)
    }
}