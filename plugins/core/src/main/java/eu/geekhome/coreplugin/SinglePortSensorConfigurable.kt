package eu.geekhome.coreplugin

import eu.geekhome.domain.automation.DeviceAutomationUnit
import eu.geekhome.domain.configurable.*
import eu.geekhome.domain.hardware.IPortFinder
import eu.geekhome.domain.hardware.PortValue
import eu.geekhome.domain.repository.InstanceDto
import java.util.*

abstract class SinglePortSensorConfigurable<T: PortValue>(
    valueType: Class<T>,
    private val portField: FieldDefinition<String>
) : SensorConfigurable<T>(valueType) {

    override val parent: Class<out Configurable?>
        get() = MetersConfigurable::class.java

    override fun buildAutomationUnit(instance: InstanceDto, portFinder: IPortFinder): DeviceAutomationUnit<T> {
        val portId = readPortId(instance)
        val port = portFinder.searchForInputPort(valueType, portId)
        return SensorAutomationUnit(port)
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

