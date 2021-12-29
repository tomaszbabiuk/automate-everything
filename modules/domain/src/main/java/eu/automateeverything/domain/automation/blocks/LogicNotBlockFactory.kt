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
import eu.automateeverything.domain.R
import eu.automateeverything.domain.automation.*

class LogicNotBlockFactory : EvaluatorBlockFactory {

    override val category = CommonBlockCategories.Logic

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
                          "check": "${Boolean::class.java.simpleName}"
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

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): EvaluatorNode {
        if (block.values == null || block.values.size != 1) {
            throw MalformedBlockException(block.type, "should have exactly one <VALUE> defined")
        }

        var nodeToNegate: EvaluatorNode? = null
        val firstValue = block.values.find { it.name == "NOT" }
        if (firstValue == null) {
            throw MalformedBlockException(block.type, "should have <value name=\"NOT\"> defined")
        } else if (firstValue.block != null) {
            nodeToNegate = transformer.transformEvaluator(firstValue.block, context)
        }

        return NotAutomationNode(nodeToNegate)
    }
}