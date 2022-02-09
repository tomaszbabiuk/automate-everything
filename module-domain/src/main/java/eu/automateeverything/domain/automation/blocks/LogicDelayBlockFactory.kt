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

class LogicDelayBlockFactory : StatementBlockFactory {

    override val category = CommonBlockCategories.Logic

    override val type: String = "logic_delay"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "${R.block_label_delay.getValue(it)}",
                  "args0": [
                    {
                      "type": "field_number",
                      "name": "DELAY",
                      "value": 0,
                      "min": 0
                    },
                    {
                      "type": "input_dummy"
                    },
                    {
                      "type": "input_statement",
                      "name": "DO"
                    }
                  ],
                  "inputsInline": true,
                  "previousStatement": null,
                  "nextStatement": null,
                  "colour": ${category.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
            """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): StatementNode {
        var doNode: StatementNode? = null

        if (block.statements != null) {
            val doStatement = block.statements.find { it.name == "DO" }
            if (doStatement == null) {
                throw MalformedBlockException(block.type, "should have <DO statement> defined")
            } else if (doStatement.block != null) {
                doNode = transformer.transformStatement(doStatement.block, context)
            }
        }

        if (block.fields == null) {
            throw MalformedBlockException(block.type, "should have <field> defined")
        }

        if (block.fields.size != 1) {
            throw MalformedBlockException(block.type, "should have only one field")
        }

        if (block.fields[0].value == null) {
            throw MalformedBlockException(block.type, "should have <field/value> with content")
        }

        val seconds = block.fields[0].value!!.toInt()

        return DelayAutomationNode(next, doNode, seconds)
    }
}