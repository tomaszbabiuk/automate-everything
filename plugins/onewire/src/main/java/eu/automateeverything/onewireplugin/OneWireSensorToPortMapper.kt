package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.container.OneWireContainer28
import com.dalsemi.onewire.container.OneWireSensor
import eu.automateeverything.domain.hardware.PortIdBuilder

class OneWireSensorToPortMapper(private val portIdBuilder: PortIdBuilder) {
    fun map(sensor: OneWireSensor): OneWirePort<*> {
        if (sensor is OneWireContainer28) {
            val inputPortId = portIdBuilder.buildPortId(sensor.addressAsString, 0, "I")
            return OneWireTemperatureInputPort(inputPortId, sensor.address, sensor.adapter)
        }

        throw Exception("Unknown sensor type")
    }
}