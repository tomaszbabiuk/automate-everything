package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.container.*
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
        when (sensor) {
            is TemperatureContainer -> {
                return mapTemperatureContainer(sensor)
            }
            is SwitchContainer -> {
                return mapSwitchContainer(sensor)
            }
            else -> {
                broadcastMessage("${sensor.name}/${sensor.addressAsString}: unsupported (ignoring)")
            }
        }

        return null
    }

    private fun mapSwitchContainer(switch: SwitchContainer): List<OneWirePort<*>>? {
        switch as OneWireContainer
        val isRelay = ds2408AsRelays.contains(switch.addressAsString)
        try {
            if (isRelay) {
                broadcastMessage("${switch.name}/${switch.addressAsString} discovered as Relay board!")

                val channelsCount = SwitchContainerHelper.readChannelsCount(switch)
                val inputReadings = SwitchContainerHelper.read(switch, false)
                return (0 until channelsCount)
                    .map { channel ->
                        val inputPortId = portIdBuilder.buildPortId(switch.addressAsString, channel, "R")
                        val initialValueRaw = inputReadings[channel].level
                        val initialValueInverted = Relay(!initialValueRaw)
                        OneWireRelayPort(inputPortId, channel, switch.address, initialValueInverted)
                    }
                    .toList()
            } else {
                broadcastMessage("${switch.name}/${switch.addressAsString} discovered as Input board!")

                val channelsCount = SwitchContainerHelper.readChannelsCount(switch)
                val inputReadings = SwitchContainerHelper.read(switch, false)
                return (0 until channelsCount)
                    .map { channel ->
                        val inputPortId = portIdBuilder.buildPortId(switch.addressAsString, channel, "B")
                        val initialValueRaw = inputReadings[channel].level
                        val initialValue = BinaryInput(initialValueRaw)
                        OneWireBinaryInputPort(inputPortId, channel, switch.address, initialValue)
                    }
                    .toList()
            }
        } catch (ex: Exception) {
            broadcastMessage("There's been a problem reading container: ${switch.name}/${switch.addressAsString}")
        }

        return null
    }

    private fun broadcastMessage(message: String) {
        eventsSink.broadcastDiscoveryEvent(owningPluginId, message)
    }

    private fun mapTemperatureContainer(temperatureSensor: TemperatureContainer): List<OneWireTemperatureInputPort>? {
        temperatureSensor as OneWireContainer
        broadcastMessage("${temperatureSensor.name}/${temperatureSensor.addressAsString} discovered as Thermometer!")

        try {
            val inputPortId = portIdBuilder.buildPortId(temperatureSensor.addressAsString, 0, "I")
            val initialValueRaw = TemperatureContainerHelper.read(temperatureSensor)
            val initialValue = Temperature(initialValueRaw + 273.15)
            return listOf(OneWireTemperatureInputPort(inputPortId, temperatureSensor.address, initialValue))
        } catch (ex: Exception) {
            broadcastMessage("There's been a problem reading container: ${temperatureSensor.name}/${temperatureSensor.addressAsString}")
        }

        return null
    }
}