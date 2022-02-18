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

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.EvaluableAutomationUnitBase
import eu.automateeverything.domain.automation.blocks.BlockCategory
import eu.automateeverything.data.hardware.PortValue
import org.pf4j.ExtensionPoint
import java.math.BigDecimal

interface Configurable : ExtensionPoint {
    val parent: Class<out Configurable>?
    val titleRes: Resource
    val descriptionRes: Resource
    val iconRaw: String
    val hasAutomation: Boolean
    val editableIcon: Boolean
    val taggable: Boolean
    val generable: Boolean

    fun <T> extractFieldValue(instance: InstanceDto, field: FieldDefinition<T>) : T {
        val rawValue = instance.fields[field.name]
        return field.builder.fromPersistableString(rawValue)
    }
}

interface ConfigurableWithFields : Configurable {
    val fieldDefinitions: Map<String, FieldDefinition<*>>
    val addNewRes: Resource
    val editRes: Resource
}

abstract class GeneratedConfigurable : NameDescriptionConfigurable(), ConfigurableWithFields {
    abstract fun generate(): InstanceDto
    override val generable: Boolean = true
    override val hasAutomation: Boolean = false
    override val taggable: Boolean = false
    override val editableIcon: Boolean = false
}

abstract class DeviceConfigurable<V>(val valueClazz: Class<V>) : NameDescriptionConfigurable(), ConfigurableWithFields {
    abstract fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<V>
    override val hasAutomation: Boolean = true
    override val taggable: Boolean = true
    override val editableIcon: Boolean = true
    override val generable: Boolean = false
}

abstract class DeviceConfigurableWithBlockCategory<V: PortValue>(valueClazz: Class<V>) : DeviceConfigurable<V>(valueClazz) {
    abstract val blocksCategory: BlockCategory
}

abstract class ControllerConfigurable<V: PortValue>(valueClazz: Class<V>) : DeviceConfigurableWithBlockCategory<V>(valueClazz) {
    abstract fun extractMinValue(instance: InstanceDto): BigDecimal
    abstract fun extractMaxValue(instance: InstanceDto): BigDecimal
}

abstract class StateDeviceConfigurable : DeviceConfigurable<State>(State::class.java) {
    abstract val states: Map<String, State>

    companion object {
        const val STATE_UNKNOWN = "unknown"
    }
}

abstract class ConditionConfigurable : NameDescriptionConfigurable(), ConfigurableWithFields {
    abstract fun buildEvaluator(instance: InstanceDto): EvaluableAutomationUnitBase
    override val hasAutomation: Boolean = false
    override val taggable: Boolean = false
    override val editableIcon: Boolean = false
    override val generable: Boolean = false
}

abstract class CategoryConfigurable : Configurable {
    override val hasAutomation: Boolean = false
    override val taggable: Boolean = false
    override val editableIcon: Boolean = false
    override val generable: Boolean = false
}

abstract class ActionConfigurable: StateDeviceConfigurable() {
    override val taggable: Boolean = false
    override val editableIcon: Boolean = true
    override val generable: Boolean = false
}

