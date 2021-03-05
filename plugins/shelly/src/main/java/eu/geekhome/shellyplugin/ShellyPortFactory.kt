package eu.geekhome.shellyplugin

import eu.geekhome.services.hardware.*
import eu.geekhome.shellyplugin.operators.*

class ShellyPortFactory {

    fun constructPorts(shellyId: String, idBuilder: PortIdBuilder, statusResponse: ShellyStatusResponse) : List<ConnectiblePort<*>> {
        val result = ArrayList<ConnectiblePort<*>>()

        if (statusResponse.relays != null) {
            for (i in statusResponse.relays.indices) {
                val relayResponse = statusResponse.relays[i]
                result.add(constructRelayInOutPort(idBuilder, shellyId, i, relayResponse))
            }

            if (statusResponse.meters != null) {
                for (i in statusResponse.meters.indices) {
                    result.add(constructRelayWattageReadPort(idBuilder, shellyId, i))
                }
            }
        }

        if (statusResponse.lights != null) {
            for (i in statusResponse.lights.indices) {
                val lightResponse = statusResponse.lights[i]
                result.add(constructPowerLevelReadWritePort(idBuilder, shellyId, i, lightResponse))
            }

            if (statusResponse.meters != null) {
                for (i in statusResponse.meters.indices) {
                    result.add(constructLightWattageReadPort(idBuilder, shellyId, i))
                }
            }
        }

        if (statusResponse.tmp != null) {
            result.add(constructTemperatureReadPort(idBuilder, shellyId, statusResponse.tmp))
        }

        if (statusResponse.hum != null) {
            result.add(constructHumidityReadPort(idBuilder, shellyId, statusResponse.hum))
        }

        if (statusResponse.bat != null) {
            result.add(constructBatteryReadPort(idBuilder, shellyId, statusResponse.bat))
        }

        return result
    }

    private fun constructBatteryReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        batteryBrief: BatteryBriefDto
    ): ConnectiblePort<*> {
        val id = idBuilder.buildPortId(shellyId, 0, "B")
        val operator = ShellyBatteryReadPortOperator(shellyId)
        operator.setValueFromBatteryResponse(batteryBrief)

        return ConnectiblePort(
            id, BatteryCharge::class.java,
            readPortOperator = operator,
        )
    }

    private fun constructHumidityReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        humidityBrief: HumidityBriefDto
    ): ConnectiblePort<*> {
        val id = idBuilder.buildPortId(shellyId, 0, "H")
        val operator = ShellyHumidityReadPortOperator(shellyId)
        operator.setValueFromHumidityResponse(humidityBrief)
        return ConnectiblePort(id, Humidity::class.java,
            readPortOperator = operator,
        )
    }

    private fun constructTemperatureReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        temperatureBrief: TemperatureBriefDto
    ): ConnectiblePort<*> {
        val id = idBuilder.buildPortId(shellyId, 0, "T")
        val operator = ShellyTemperatureReadPortOperator(shellyId)
        operator.setValueFromTemperatureResponse(temperatureBrief)
        return ConnectiblePort(id, Temperature::class.java,
            readPortOperator = operator,
        )
    }

    private fun constructRelayInOutPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        relayResponse: RelayResponseDto
    ): ConnectiblePort<*> {
        val id = idBuilder.buildPortId(shellyId, channel, "R")
        val operator = ShellyRelayReadWritePortOperator(shellyId, channel)
        operator.setValueFromRelayResponse(relayResponse)
        return ConnectiblePort(id, Relay::class.java,
            readPortOperator = operator,
            writePortOperator = operator
        )
    }

    private fun constructRelayWattageReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int
    ): ConnectiblePort<*> {
        val id = idBuilder.buildPortId(shellyId, channel, "W")
        val operator = ShellyRelayWattageReadPortOperator(shellyId, channel)
        return ConnectiblePort(id, Wattage::class.java,
            readPortOperator = operator
        )
    }

    private fun constructPowerLevelReadWritePort(
        idBuilder: PortIdBuilder,
        shellyId : String,
        channel: Int,
        lightResponse: LightBriefDto
    ): ConnectiblePort<*> {
        val operator = PowerLevelReadWritePortOperator(shellyId, channel)
        operator.setValueFromLightResponse(lightResponse)
        val id = idBuilder.buildPortId(shellyId, channel, "L")
        return ConnectiblePort(id, PowerLevel::class.java,
            readPortOperator = operator,
            writePortOperator = operator
        )
    }

    private fun constructLightWattageReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int
    ) : ConnectiblePort<*> {
        val operator = LightWattageReadPortOperator(shellyId, channel)
        val id = idBuilder.buildPortId(shellyId, channel, "W")
        return ConnectiblePort(id, Wattage::class.java,
            readPortOperator = operator
        )
    }

}