package eu.geekhome.shellyplugin

import eu.geekhome.services.hardware.*
import eu.geekhome.shellyplugin.operators.*

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
                result.add(constructRelayInOutPort(idBuilder, shellyId, i, relayResponse, sleepInterval))
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

        if (statusResponse.tmp != null) {
            result.add(constructTemperatureReadPort(idBuilder, shellyId, statusResponse.tmp, sleepInterval))
        }

        if (statusResponse.hum != null) {
            result.add(constructHumidityReadPort(idBuilder, shellyId, statusResponse.hum, sleepInterval))
        }

        if (statusResponse.bat != null) {
            result.add(constructBatteryReadPort(idBuilder, shellyId, statusResponse.bat, sleepInterval))
        }

        return result
    }

    private fun constructBatteryReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        batteryBrief: BatteryBriefDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, 0, "B")
        val operator = ShellyBatteryReadPortOperator(shellyId)
        operator.setValueFromBatteryResponse(batteryBrief)

        return ShellyPort(sleepInterval,
            id, BatteryCharge::class.java,
            readPortOperator = operator,
        )
    }

    private fun constructHumidityReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        humidityBrief: HumidityBriefDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, 0, "H")
        val operator = ShellyHumidityReadPortOperator(shellyId)
        operator.setValueFromHumidityResponse(humidityBrief)
        return ShellyPort(sleepInterval, id, Humidity::class.java,
            readPortOperator = operator,
        )
    }

    private fun constructTemperatureReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        temperatureBrief: TemperatureBriefDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, 0, "T")
        val operator = ShellyTemperatureReadPortOperator(shellyId)
        operator.setValueFromTemperatureResponse(temperatureBrief)
        return ShellyPort(sleepInterval, id, Temperature::class.java,
            readPortOperator = operator,
        )
    }

    private fun constructRelayInOutPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        relayResponse: RelayResponseDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, channel, "R")
        val operator = ShellyRelayReadWritePortOperator(shellyId, channel)
        operator.setValueFromRelayResponse(relayResponse)
        return ShellyPort(sleepInterval, id, Relay::class.java,
            readPortOperator = operator,
            writePortOperator = operator
        )
    }

    private fun constructRelayWattageReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        sleepInterval: Long
    ): ShellyPort<*> {
        val id = idBuilder.buildPortId(shellyId, channel, "W")
        val operator = ShellyRelayWattageReadPortOperator(shellyId, channel)
        return ShellyPort(sleepInterval, id, Wattage::class.java,
            readPortOperator = operator
        )
    }

    private fun constructPowerLevelReadWritePort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        lightResponse: LightBriefDto,
        sleepInterval: Long
    ): ShellyPort<*> {
        val operator = PowerLevelReadWritePortOperator(shellyId, channel)
        operator.setValueFromLightResponse(lightResponse)
        val id = idBuilder.buildPortId(shellyId, channel, "L")
        return ShellyPort(sleepInterval, id, PowerLevel::class.java,
            readPortOperator = operator,
            writePortOperator = operator
        )
    }

    private fun constructLightWattageReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        sleepInterval: Long
    ) : ShellyPort<*> {
        val operator = LightWattageReadPortOperator(shellyId, channel)
        val id = idBuilder.buildPortId(shellyId, channel, "W")
        return ShellyPort(sleepInterval, id, Wattage::class.java,
            readPortOperator = operator
        )
    }

}