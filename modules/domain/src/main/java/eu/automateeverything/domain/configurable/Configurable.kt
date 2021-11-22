package eu.automateeverything.domain.configurable

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.EvaluableAutomationUnitBase
import eu.automateeverything.domain.automation.blocks.BlockCategory
import eu.automateeverything.data.hardware.PortValue
import org.pf4j.ExtensionPoint

interface Configurable : ExtensionPoint {
    val parent: Class<out Configurable>?
    val titleRes: Resource
    val descriptionRes: Resource
    val iconRaw: String
    val hasAutomation: Boolean
    val editableIcon: Boolean
    val taggable: Boolean

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

abstract class DeviceConfigurable<V>(val valueClazz: Class<V>) : NameDescriptionConfigurable(), ConfigurableWithFields {
    abstract fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<V>
    override val hasAutomation: Boolean = false
    override val taggable: Boolean = true
    override val editableIcon: Boolean = true
}

abstract class SensorConfigurable<V: PortValue>(valueClazz: Class<V>) : DeviceConfigurable<V>(valueClazz) {
    abstract val blocksCategory: BlockCategory
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
}

abstract class CategoryConfigurable : Configurable {
    override val hasAutomation: Boolean = false
    override val taggable: Boolean = false
    override val editableIcon: Boolean = false
}

abstract class ActionConfigurable: StateDeviceConfigurable() {
    override val taggable: Boolean = false
    override val editableIcon: Boolean = false
}

