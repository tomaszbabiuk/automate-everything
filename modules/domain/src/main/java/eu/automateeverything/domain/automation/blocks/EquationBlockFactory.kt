/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
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
import java.math.BigDecimal

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
        val right = rightValue.value!!.toBigDecimal()

        val leftValue = block.values?.find { it.name == "LEFT" }

        var leftNode: ValueNode? = null
        if (leftValue?.block != null) {
            leftNode = transformer.transformValue(leftValue.block, context)
        }

        return EquationAutomationNode(valueType, leftNode, operator, right)
    }
}