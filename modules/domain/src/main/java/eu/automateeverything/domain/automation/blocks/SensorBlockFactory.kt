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
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.*
import eu.automateeverything.domain.configurable.DeviceConfigurable

class SensorBlockFactory<T : PortValue>(
    private val valueType: Class<T>,
    override val category: BlockCategory,
    private val sensorId: Long,
    private val label: Resource
) : ValueBlockFactory {

    override val type: String = "${valueType.simpleName}_sensor_$sensorId"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                   { "type":  "$type",
                     "colour": ${category.color},
                     "tooltip": null,
                     "helpUrl": null,
                     "message0": "${label.getValue(it)}",
                     "output": "${valueType.simpleName}" }
                """
                .trimIndent()
        }
    }

    override fun transform(
        block: Block,
        next: StatementNode?,
        context: AutomationContext,
        transformer: BlocklyTransformer,
    ): ValueNode {
        val evaluator = context.automationUnitsCache[this.sensorId]

        if (evaluator != null) {
            return SensorAutomationNode(evaluator)
        }

        throw MalformedBlockException(
            block.type,
            "it's impossible to connect this block with correct ${DeviceConfigurable::class.java}"
        )
    }

    override fun dependsOn(): List<Long> {
        return listOf(sensorId)
    }
}
