package eu.automateeverything.shellyplugin

import eu.automateeverything.domain.hardware.*
import eu.automateeverything.shellyplugin.ports.*

class ShellyPortFactory {

    fun constructPorts(
        shellyId: String,
        idBuilder: PortIdBuilder,
        statusResponse: ShellyStatusResponse,
        settingsResponse: ShellySettingsResponse
    ) : List<ShellyPort<*>> {
        val result = ArrayList<ShellyPort<*>>()
        var sleepInterval = 60000L
        if (settingsResponse.sleep_mode != null) {
            sleepInterval += settingsResponse.sleep_mode.period * settingsResponse.sleep_mode.unit.minutes * 60000L
        }
        if (statusResponse.relays != null) {
            for (i in statusResponse.relays.indices) {
                val relayResponse = statusResponse.relays[i]
                result.add(constructRelayOutputPort(idBuilder, shellyId, i, relayResponse, sleepInterval))
            }

            if (statusResponse.meters != null) {
                for (i in statusResponse.meters.indices) {
                    result.add(constructRelayWattageReadPort(idBuilder, shellyId, i, sleepInterval))
                }
            }
        }

        if (statusResponse.lights != null) {
            for (i in statusResponse.lights.indices) {
                val lightResponse = statusResponse.lights[i]
                result.add(constructPowerLevelReadWritePort(idBuilder, shellyId, i, lightResponse, sleepInterval))
            }

            if (statusResponse.meters != null) {
                for (i in statusResponse.meters.indices) {
                    result.add(constructLightWattageReadPort(idBuilder, shellyId, i, sleepInterval))
                }
            }
        }

        if (statusResponse.inputs != null) {
            for (i in statusResponse.inputs.indices) {
                result.add(constructBooleanReadPort(idBuilder, shellyId, i, statusResponse.inputs[i], sleepInterval))
            }
        }

        if (statusResponse.tmp != null) {
            result.add(constructTemperatureReadPort(idBuilder, shellyId, statusResponse.tmp, sleepInterval))
        }

        if (statusResponse.hum != null) {
            result.add(constructHumidityReadPort(idBuilder, shellyId, statusResponse.hum, sleepInterval))
        }

        if (statusResponse.bat != null) {
            result.add(constructBatteryReadPort(idBuilder, shellyId, statusResponse.bat, sleepInterval))
        }

        if (statusResponse.lux != null) {
            result.add(constructLuminosityReadPort(idBuilder, shellyId, statusResponse.lux, sleepInterval))
        }

        if (statusResponse.sensor != null) {
            result.add(constructStateReadPort(idBuilder, shellyId, statusResponse.sensor, sleepInterval))
        }

        if (statusResponse.accel != null) {
            result.add(constructVibrationReadPort(idBuilder, shellyId, statusResponse.accel, sleepInterval))
        }

        return result
    }

    private fun constructBooleanReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        inputBriefDto: InputBriefDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, channel.toString(), "I")
        val port = ShellyBinaryInputPort(id, shellyId, channel, sleepInterval)
        port.setValueFromInputResponse(inputBriefDto)

        return port
    }

    private fun constructBatteryReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        batteryBrief: BatteryBriefDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, 0.toString(), "B")
        val port = ShellyBatteryInputPort(id, shellyId, sleepInterval)
        port.setValueFromBatteryResponse(batteryBrief)

        return port
    }

    private fun constructHumidityReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        humidityBrief: HumidityBriefDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, 0.toString(), "H")
        val port = ShellyHumidityInputPort(id, shellyId, sleepInterval)
        port.setValueFromHumidityResponse(humidityBrief)
        return port
    }

    private fun constructLuminosityReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        luminosityBrief: LuminosityBriefDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, 0.toString(), "L")
        val port = ShellyLuminosityInputPort(id, shellyId, sleepInterval)
        port.setValueFromLuminosityResponse(luminosityBrief)
        return port
    }

    private fun constructStateReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        stateBrief: StateBriefDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, "state", "I")
        val port = ShellyStateInputPort(id, shellyId, sleepInterval)
        port.setValueFromStateResponse(stateBrief)
        return port
    }

    private fun constructVibrationReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        accelBrief: AccelBriefDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, "vibration", "I")
        val port = ShellyVibrationInputPort(id, shellyId, sleepInterval)
        port.setValueFromAccelResponse(accelBrief)
        return port
    }

    private fun constructTemperatureReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        temperatureBrief: TemperatureBriefDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, 0.toString(), "T")
        val port = ShellyTemperatureInputPort(id, shellyId, sleepInterval)
        port.setValueFromTemperatureResponse(temperatureBrief)
        return port
    }

    private fun constructRelayOutputPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        relayResponse: RelayResponseDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, channel.toString(), "R")
        val port = ShellyRelayOutputPort(id, shellyId, channel, sleepInterval)
        port.setValueFromRelayResponse(relayResponse)
        return port
    }

    private fun constructPowerLevelReadWritePort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        lightResponse: LightBriefDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, channel.toString(), "L")
        val port = ShellyPowerLevelOutputPort(id, shellyId, channel, sleepInterval)
        port.setValueFromLightResponse(lightResponse)
        return port
    }

    private fun constructLightWattageReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        sleepInterval: Long
    ) : ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, channel.toString(), "W")
        return ShellyWattageInputPort(id, shellyId, channel, sleepInterval, TopicSource.Light)
    }

    private fun constructRelayWattageReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, channel.toString(), "W")
        return ShellyWattageInputPort(id, shellyId, channel, sleepInterval, TopicSource.Relay)
    }

}