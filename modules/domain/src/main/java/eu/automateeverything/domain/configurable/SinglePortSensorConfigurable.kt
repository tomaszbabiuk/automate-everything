package eu.automateeverything.domain.configurable

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.DeviceAutomationUnit
import eu.automateeverything.domain.hardware.PortFinder
import eu.automateeverything.domain.hardware.PortValue
import eu.automateeverything.domain.automation.SensorAutomationUnit
import java.util.*

abstract class SinglePortSensorConfigurable<T: PortValue>(
    valueClazz: Class<T>,
    private val portField: FieldDefinition<String>,
    private val portFinder: PortFinder
) : SensorConfigurable<T>(valueClazz) {

    override fun buildAutomationUnit(instance: InstanceDto): DeviceAutomationUnit<T> {
        val portId = readPortId(instance)
        val port = portFinder.searchForInputPort(valueClazz, portId)
        val name = instance.fields[FIELD_NAME]
        return SensorAutomationUnit(name, port)
    }

    private fun readPortId(instance: InstanceDto): String {
        val portFieldValue = instance.fields[FIELD_PORT]
        return portField.builder.fromPersistableString(portFieldValue)
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

