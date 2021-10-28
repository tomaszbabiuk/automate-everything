package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.container.OneWireContainer
import com.dalsemi.onewire.container.OneWireContainer28
import com.dalsemi.onewire.container.SwitchContainer
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.hardware.BinaryInput
import eu.automateeverything.domain.hardware.PortIdBuilder
import eu.automateeverything.domain.hardware.Temperature
import eu.automateeverything.onewireplugin.helpers.SwitchContainerHelper
import eu.automateeverything.onewireplugin.helpers.TemperatureContainerHelper

class OneWireSensorToPortMapper(
    private val owningPluginId: String,
    private val portIdBuilder: PortIdBuilder,
    private val eventsSink: EventsSink) {

    fun map(sensor: OneWireContainer): List<OneWirePort<*>>? {
        val adapter = sensor.adapter
        when (sensor) {
            is OneWireContainer28 -> {
                try {
                    val inputPortId = portIdBuilder.buildPortId(sensor.addressAsString, 0, "I")
                    val initialValueRaw = TemperatureContainerHelper.read(adapter, sensor.address)
                    val initialValue = Temperature(initialValueRaw + 273.15)
                    return listOf(OneWireTemperatureInputPort(inputPortId, sensor.address, initialValue))
                } catch (ex:Exception) {
                    val message = "There's been a problem reading container: ${sensor.name}/${sensor.addressAsString}"
                    eventsSink.broadcastDiscoveryEvent(owningPluginId, message)
                }
            }
            /*is SwitchContainer -> { //as binary input
                try {
                    val channelsCount = SwitchContainerHelper.readChannelsCount(sensor)
                    val inputReadings = SwitchContainerAsBinaryInputHelper.read(sensor, false)
                    return (0 until channelsCount)
                        .map { channel ->
                            val inputPortId = portIdBuilder.buildPortId(sensor.addressAsString, channel, "B")
                            val initialValueRaw = inputReadings[channel].level
                            val initialValue = BinaryInput(initialValueRaw)
                            OneWireBinaryInputPort(inputPortId, channel, sensor.address, initialValue)
                        }
                        .toList()
                }
                catch (ex: Exception) {
                    val message = "There's been a problem reading container: ${sensor.name}/${sensor.addressAsString}"
                    eventsSink.broadcastDiscoveryEvent(owningPluginId, message)
                }
            }*/
            is SwitchContainer -> { //as relay
                try {
                    val channelsCount = SwitchContainerHelper.readChannelsCount(sensor)
                    val inputReadings = SwitchContainerHelper.read(sensor, false)
                    return (0 until channelsCount)
                        .map { channel ->
                            val inputPortId = portIdBuilder.buildPortId(sensor.addressAsString, channel, "B")
                            val initialValueRaw = inputReadings[channel].level
                            val initialValue = BinaryInput(initialValueRaw)
                            OneWireBinaryInputPort(inputPortId, channel, sensor.address, initialValue)
                        }
                        .toList()
                }
                catch (ex: Exception) {
                    val message = "There's been a problem reading container: ${sensor.name}/${sensor.addressAsString}"
                    eventsSink.broadcastDiscoveryEvent(owningPluginId, message)
                }
            }
            else -> {
                val message = "Unsupported container type: ${sensor.name}/${sensor.addressAsString}"
                eventsSink.broadcastDiscoveryEvent(owningPluginId, message)
            }
        }

        return null
    }
}