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

package eu.automateeverything.domain.configurable

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.hardware.PortFinder
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.SensorAutomationUnit
import eu.automateeverything.domain.automation.StateChangeReporter
import java.util.*

abstract class SinglePortDeviceConfigurable<T: PortValue>(
    valueClazz: Class<T>,
    private val stateChangeReporter: StateChangeReporter,
    private val portField: FieldDefinition<String>,
    private val portFinder: PortFinder
) : DeviceConfigurableWithBlockCategory<T>(valueClazz) {

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<T> {
        val portId = extractFieldValue(instance, portField)
        val port = portFinder.searchForInputPort(valueClazz, portId)
        val name = instance.fields[FIELD_NAME]!!
        return SensorAutomationUnit(stateChangeReporter, instance, name, port)
    }

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> = HashMap(super.fieldDefinitions)
            result[FIELD_PORT] = portField
            return result
        }

    companion object {
        const val FIELD_PORT = "portId"
    }
}

