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

class LogicIfElseBlockFactory : StatementBlockFactory {

    override val category = CommonBlockCategories.Logic

    override val type: String = "logic_if_than_else"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "${R.block_label_if_than_else.getValue(it)}",
                  "args0": [
                    {
                      "type": "input_value",
                      "name": "CONDITION",
                      "check": "${Boolean::class.java.simpleName}",
                      "align": "RIGHT"
                    },
                    {
                      "type": "input_statement",
                      "name": "IF",
                      "align": "RIGHT"
                    },
                    {
                      "type": "input_statement",
                      "name": "ELSE",
                      "align": "RIGHT"
                    }
                  ],
                  "previousStatement": null,
                  "nextStatement": null,
                  "colour": ${category.color},
                  "tooltip": null,
                  "helpUrl": null
                }
            """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): StatementNode {
        var ifNode: StatementNode? = null
        var elseNode: StatementNode? = null

        if (block.statements != null) {
            val ifStatement = block.statements.find { it.name == "IF" }
            if (ifStatement == null) {
                throw MalformedBlockException(block.type, "should have <IF statement> defined")
            } else if (ifStatement.block != null) {
                ifNode = transformer.transformStatement(ifStatement.block, context)
            }

            val elseStatement = block.statements.find { it.name == "ELSE" }
            if (elseStatement == null) {
                throw MalformedBlockException(block.type, "should have <ELSE statement> defined")
            } else if (elseStatement.block != null) {
                elseNode = transformer.transformStatement(elseStatement.block, context)
            }
        }

        if (block.values == null) {
            throw MalformedBlockException(block.type, "should have at least <VALUE> defined")
        }

        if (block.values.size != 1) {
            throw MalformedBlockException(block.type, "should have only one <VALUE>")
        }

        val valueNode = transformer.transformEvaluator(block.values[0].block!!, context)

        return IfThanElseAutomationNode(next, valueNode, ifNode, elseNode)
    }
}