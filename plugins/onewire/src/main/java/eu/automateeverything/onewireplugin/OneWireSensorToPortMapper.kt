package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.container.OneWireContainer
import com.dalsemi.onewire.container.OneWireContainer28
import com.dalsemi.onewire.container.SwitchContainer
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.hardware.PortIdBuilder

class OneWireSensorToPortMapper(
    private val owningPluginId: String,
    private val portIdBuilder: PortIdBuilder,
    private val eventsSink: EventsSink) {

    fun map(sensor: OneWireContainer): List<OneWirePort<*>>? {
        when (sensor) {
            is OneWireContainer28 -> {
                val inputPortId = portIdBuilder.buildPortId(sensor.addressAsString, 0, "I")
                return listOf(OneWireTemperatureInputPort(inputPortId, sensor.address, sensor.adapter))
            }
            is SwitchContainer -> {
                val coordinator = BinaryInputsCoordinator(sensor)
                val channelsCount = coordinator.channelsCount
                val inputReadings = coordinator.tryRead(false)
                return (0 until channelsCount)
                    .map { channel ->
                        val inputPortId = portIdBuilder.buildPortId(sensor.addressAsString, channel, "B")
                        val initialValue = inputReadings[channel].level
                        OneWireBinaryInputPort(inputPortId, channel, sensor.address, initialValue)
                    }
                    .toList()
            }
            else -> {
                val message = "Unsupported container type: ${sensor.javaClass.simpleName}/${sensor.addressAsString}"
                eventsSink.broadcastDiscoveryEvent(owningPluginId, message)
            }
        }

        return null
    }
}