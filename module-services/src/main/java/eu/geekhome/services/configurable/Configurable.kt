package eu.geekhome.services.configurable

import eu.geekhome.services.automation.DeviceAutomationUnit
import eu.geekhome.services.automation.EvaluableAutomationUnit
import eu.geekhome.services.automation.State
import eu.geekhome.services.hardware.IPortFinder
import eu.geekhome.services.localization.Resource
import eu.geekhome.services.localization.ResourceWithId
import eu.geekhome.services.repository.InstanceDto
import org.pf4j.ExtensionPoint

interface Configurable : ExtensionPoint {
    val fieldDefinitions: Map<String, FieldDefinition<*>>
    val parent: Class<out Configurable>
    val addNewRes: Resource
    val editRes: Resource
    val titleRes: Resource
    val descriptionRes: Resource
    val iconRaw: String?

    fun getType() : ConfigurableType  {
        if (this is ConditionConfigurable) {
            return ConfigurableType.Condition
        }

        if (this is StateDeviceConfigurable) {
            return ConfigurableType.StateDevice
        }

        return ConfigurableType.Other
    }
}

interface ConditionConfigurable : Configurable {
    fun buildEvaluator(instance: InstanceDto): EvaluableAutomationUnit
}

interface StateDeviceConfigurable : Configurable {
    fun buildEvaluator(instance: InstanceDto, portFinder: IPortFinder): DeviceAutomationUnit<*>
    val blockTargets: List<ResourceWithId>
    val states: Map<String, State>
}