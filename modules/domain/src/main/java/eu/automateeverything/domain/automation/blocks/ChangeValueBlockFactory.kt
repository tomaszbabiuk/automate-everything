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
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.domain.R
import eu.automateeverything.domain.automation.*
import eu.automateeverything.domain.configurable.ControllerConfigurable
import eu.automateeverything.domain.hardware.*
import java.lang.Exception

open class ChangeValueBlockFactory<T: PortValue>(
    private val valueClazz: Class<T>) : StatementBlockFactory {

    override val category: BlockCategory = CommonBlockCategories.ThisObject

    override val type: String = "change_value"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "${R.block_label_change_value.getValue(it)}",
                  "args0": [
                    {
                      "type": "input_dummy"
                    },
                    {
                      "type": "input_value",
                      "name": "VALUE",
                      "check": "${valueClazz.simpleName}"
                    }
                  ],
                  "inputsInline": true,
                  "previousStatement": null,
                  "nextStatement": null,
                  "colour": ${matchColor()},
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    private fun matchColor(): Int {
        return when (valueClazz) {
            Temperature::class.java -> {
                CommonBlockCategories.Temperature.color
            }
            Humidity::class.java -> {
                CommonBlockCategories.Humidity.color
            }
            Wattage::class.java -> {
                CommonBlockCategories.Wattage.color
            }
            PowerLevel::class.java -> {
                CommonBlockCategories.PowerLevel.color
            }
            else -> CommonBlockCategories.ThisObject.color
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun transform(
        block: Block,
        next: StatementNode?,
        context: AutomationContext,
        transformer: BlocklyTransformer
    ): StatementNode {
        if (block.values == null || block.values.size != 1) {
            throw MalformedBlockException(block.type, "should have exactly one VALUE defined")
        }

        val valueField = block.values.find { it.name == "VALUE" }
            ?: throw MalformedBlockException(block.type, "VALUE field not found")

        var valueNode: ValueNode? = null
        if (valueField.block != null) {
            valueNode = transformer.transformValue(valueField.block, context)
        }

        if (context.thisDevice is ControllerConfigurable<*>) {
            val unit = context.automationUnitsCache[context.instance.id] as? ControllerAutomationUnit<in PortValue>
            if (unit != null) {
                return ChangeValueAutomationNode(valueNode, unit, next)
            } else {
                throw Exception("Invalid automation unit class, ${ControllerAutomationUnit::class.java.simpleName} expected")
            }
        }

        throw MalformedBlockException(block.type, "it's impossible to connect this block with correct ${ControllerConfigurable::class.java}")
    }
}