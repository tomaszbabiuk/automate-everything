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

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.R
import eu.automateeverything.domain.automation.*

class StateChangeTriggerBlockFactory(
    private val instanceId: Long,
    private val deviceName: String,
    private val states: Map<String, State>) : TriggerBlockFactory {

    override val category = CommonBlockCategories.Triggers

    private val typePrefix = "trigger_statedevice_"
    override val type: String = typePrefix + instanceId

    override fun buildBlock(): RawJson {

        return RawJson {
            """
               {
               "type": "$type",
               "message0": "\"$deviceName\" ${R.block_label_changes_state.getValue(it)}",
               "args0": [
                {
                  "type": "input_dummy",
                  "align": "CENTRE"
                },
                 {
                   "type": "field_dropdown",
                   "name": "STATE_ID",
                   "options": [
                     ${buildStateOptions(states, it)}
                   ]
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
        transformer: BlocklyTransformer
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

        val stateId = block.fields[0].value!!

        val instanceIdRaw = block.type.replace(typePrefix, "")
        val instanceId = instanceIdRaw.toLong()
        val triggerUnit = context.automationUnitsCache[instanceId] as StateDeviceAutomationUnitBase

        return StateDeviceTriggerNode(context, instanceId, triggerUnit, stateId, next)
    }

    override fun dependsOn(): List<Long> {
        return listOf(instanceId)
    }
}