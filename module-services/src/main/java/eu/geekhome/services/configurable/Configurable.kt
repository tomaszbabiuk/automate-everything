package eu.geekhome.services.configurable

import eu.geekhome.services.automation.EvaluableAutomationUnit
import eu.geekhome.services.automation.IDeviceAutomationUnit
import eu.geekhome.services.automation.State
import eu.geekhome.services.hardware.IPortFinder
import eu.geekhome.services.localization.Resource
import eu.geekhome.services.repository.InstanceDto
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
    abstract fun buildAutomationUnit(instance: InstanceDto, portFinder: IPortFinder): IDeviceAutomationUnit<*>
    abstract val states: Map<String, State>
    override val hasAutomation: Boolean = true
    override val taggable: Boolean = true
    override val editableIcon: Boolean = true
}

abstract class CategoryConfigurable : Configurable {
    override val hasAutomation: Boolean = false
    override val taggable: Boolean = false
    override val editableIcon: Boolean = false
}

abstract class SensorConfigurable : NameDescriptionConfigurable(), ConfigurableWithFields {
    override val hasAutomation: Boolean = false
    override val taggable: Boolean = true
    override val editableIcon: Boolean = true
}