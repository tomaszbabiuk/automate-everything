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
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.R
import eu.automateeverything.domain.automation.*

class TimeloopTriggerBlockFactory : TriggerBlockFactory {

    enum class LoopTime(val label: Resource, val seconds: Int) {
        Second(R.second, 1),
        Seconds15(R.seconds15, 15),
        Seconds30(R.seconds30, 30),
        Minute(R.minute, 60),
        Minutes2(R.minutes2, 120),
        Minutes5(R.minutes5, 300),
        Minutes10(R.minutes10, 600),
        Minutes30(R.minutes30, 1800),
        Hour(R.hour, 3600);
    }

    override val category = CommonBlockCategories.Triggers

    override val type: String = "trigger_timeloop"

    override fun buildBlock(): RawJson {
        return RawJson { language ->
            """
                   {
                   "type": "$type",
                   "message0": "${R.block_label_repeat.getValue(language)}",
                   "args0": [
                     {
                       "type": "field_dropdown",
                       "name": "SECONDS",
                       "options": ${LoopTime.values().joinToString(prefix="[", postfix = "]") { "[\"${it.label.getValue(language)}\", \"${it.seconds}\"]" }}
                     }
                   ],
                   "nextStatement": "${Boolean::class.java.simpleName}",
                   "colour": ${category.color},
                   "tooltip": null,
                   "helpUrl": null
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
    ): StatementNode {

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

        return TimeTriggerNode(seconds, next)
    }
}