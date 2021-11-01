package eu.automateeverything.domain.configurable

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.DeviceAutomationUnit
import eu.automateeverything.domain.hardware.PortFinder
import eu.automateeverything.domain.hardware.PortValue
import eu.automateeverything.domain.automation.SensorAutomationUnit
import java.util.*

abstract class SinglePortDeviceConfigurable<T: PortValue>(
    valueClazz: Class<T>,
    private val portField: FieldDefinition<String>,
    private val portFinder: PortFinder
) : DeviceConfigurable<T>(valueClazz) {

    override fun buildAutomationUnit(instance: InstanceDto): DeviceAutomationUnit<T> {
        val portId = extractFieldValue(instance, portField)
        val port = portFinder.searchForInputPort(valueClazz, portId)
        val name = instance.fields[FIELD_NAME]
        return SensorAutomationUnit(name, port)
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

