package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.adapter.DSPortAdapter
import com.dalsemi.onewire.container.OneWireContainer
import com.dalsemi.onewire.container.OneWireContainer28
import com.dalsemi.onewire.container.OneWireContainer29
import com.dalsemi.onewire.container.SwitchContainer
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.hardware.BinaryInput
import eu.automateeverything.domain.hardware.PortIdBuilder
import eu.automateeverything.domain.hardware.Relay
import eu.automateeverything.domain.hardware.Temperature
import eu.automateeverything.onewireplugin.helpers.SwitchContainerHelper
import eu.automateeverything.onewireplugin.helpers.TemperatureContainerHelper

class OneWireSensorToPortMapper(
    private val owningPluginId: String,
    private val portIdBuilder: PortIdBuilder,
    private val eventsSink: EventsSink,
    private val ds2408AsRelays: List<String>
) {
    fun map(sensor: OneWireContainer): List<OneWirePort<*>>? {
        val adapter = sensor.adapter
        when (sensor) {
            is OneWireContainer28 -> {
                return mapOneWireContainer28(adapter, sensor)
            }
            is OneWireContainer29 -> {
                mapOneWireContainer29(adapter, sensor)
            }
            else -> {
                broadcastMessage("${sensor.name}/${sensor.addressAsString}: unsupported (ignoring)")
            }
        }

        return null
    }

    private fun mapOneWireContainer29(adapter: DSPortAdapter, sensor: SwitchContainer): List<OneWirePort<*>>? {
        val isRelay = ds2408AsRelays.contains((sensor as OneWireContainer).addressAsString)
        try {
            if (isRelay) {
                broadcastMessage("${sensor.name}/${sensor.addressAsString} discovered as Relay board!")

                val channelsCount = SwitchContainerHelper.readChannelsCount(adapter, sensor.address)
                val inputReadings = SwitchContainerHelper.read(sensor, false)
                return (0 until channelsCount)
                    .map { channel ->
                        val inputPortId = portIdBuilder.buildPortId((sensor as OneWireContainer).addressAsString, channel, "R")
                        val initialValueRaw = inputReadings[channel].level
                        val initialValue = Relay(initialValueRaw)
                        OneWireRelayPort(inputPortId, channel, sensor.address, initialValue)
                    }
                    .toList()
            } else {
                broadcastMessage("${sensor.name}/${sensor.addressAsString} discovered as Relay board!")

                val channelsCount = SwitchContainerHelper.readChannelsCount(adapter, sensor.address)
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
        } catch (ex: Exception) {
            broadcastMessage("There's been a problem reading container: ${sensor.name}/${sensor.addressAsString}")
        }

        return null
    }

    private fun broadcastMessage(message: String) {
        eventsSink.broadcastDiscoveryEvent(owningPluginId, message)
    }

    private fun mapOneWireContainer28(adapter: DSPortAdapter, sensor: OneWireContainer28): List<OneWireTemperatureInputPort>? {
        broadcastMessage("${sensor.name}/${sensor.addressAsString} discovered as Thermometer!")

        try {
            val inputPortId = portIdBuilder.buildPortId(sensor.addressAsString, 0, "I")
            val initialValueRaw = TemperatureContainerHelper.read(adapter, sensor.address)
            val initialValue = Temperature(initialValueRaw + 273.15)
            return listOf(OneWireTemperatureInputPort(inputPortId, sensor.address, initialValue))
        } catch (ex: Exception) {
            broadcastMessage("There's been a problem reading container: ${sensor.name}/${sensor.addressAsString}")
        }

        return null
    }
}