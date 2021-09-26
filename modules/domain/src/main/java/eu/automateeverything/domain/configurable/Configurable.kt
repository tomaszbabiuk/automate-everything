package eu.automateeverything.domain.configurable

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.EvaluableAutomationUnit
import eu.automateeverything.domain.automation.DeviceAutomationUnit
import eu.automateeverything.domain.hardware.*
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.blocks.BlockCategory
import org.pf4j.ExtensionPoint

interface Configurable : ExtensionPoint {
    val parent: Class<out Configurable>?
    val titleRes: Resource
    val descriptionRes: Resource
    val iconRaw: String
    val hasAutomation: Boolean
    val editableIcon: Boolean
    val taggable: Boolean
}

interface ConfigurableWithFields : Configurable {
    val fieldDefinitions: Map<String, FieldDefinition<*>>
    val addNewRes: Resource
    val editRes: Resource
}

abstract class ConditionConfigurable : NameDescriptionConfigurable(), ConfigurableWithFields {
    abstract fun buildEvaluator(instance: InstanceDto): EvaluableAutomationUnit
    override val hasAutomation: Boolean = false
    override val taggable: Boolean = false
    override val editableIcon: Boolean = false
}

abstract class StateDeviceConfigurable : NameDescriptionConfigurable(), ConfigurableWithFields {
    abstract fun buildAutomationUnit(instance: InstanceDto): DeviceAutomationUnit<State>
    abstract val states: Map<String, State>
    override val hasAutomation: Boolean = true
    override val taggable: Boolean = true
    override val editableIcon: Boolean = true

    companion object {
        const val STATE_UNKNOWN = "unknown"
    }
}

abstract class SensorConfigurable<V: PortValue>(val valueClazz: Class<V>) : NameDescriptionConfigurable(), ConfigurableWithFields {
    abstract fun buildAutomationUnit(instance: InstanceDto): DeviceAutomationUnit<V>
    override val hasAutomation: Boolean = false
    override val taggable: Boolean = true
    override val editableIcon: Boolean = true
    abstract val blocksCategory: BlockCategory
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

