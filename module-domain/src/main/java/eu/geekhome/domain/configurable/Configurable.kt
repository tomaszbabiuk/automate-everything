package eu.geekhome.domain.configurable

import eu.geekhome.data.automation.State
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.domain.automation.EvaluableAutomationUnit
import eu.geekhome.domain.automation.DeviceAutomationUnit
import eu.geekhome.domain.hardware.*
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.automation.StateChangeReporter
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
    abstract fun buildAutomationUnit(instance: InstanceDto, portFinder: IPortFinder, stateChangeReporter: StateChangeReporter): DeviceAutomationUnit<State>
    abstract val states: Map<String, State>
    override val hasAutomation: Boolean = true
    override val taggable: Boolean = true
    override val editableIcon: Boolean = true

    companion object {
        const val STATE_UNKNOWN = "unknown"
    }
}

abstract class SensorConfigurable<V: PortValue>(val valueType: Class<V>) : NameDescriptionConfigurable(), ConfigurableWithFields {
    abstract fun buildAutomationUnit(instance: InstanceDto, portFinder: IPortFinder): DeviceAutomationUnit<V>
    override val hasAutomation: Boolean = false
    override val taggable: Boolean = true
    override val editableIcon: Boolean = true
}

abstract class CategoryConfigurable : Configurable {
    override val hasAutomation: Boolean = false
    override val taggable: Boolean = false
    override val editableIcon: Boolean = false
}

