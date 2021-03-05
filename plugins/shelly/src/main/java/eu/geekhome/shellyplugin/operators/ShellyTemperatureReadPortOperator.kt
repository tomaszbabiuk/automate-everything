package eu.geekhome.shellyplugin.operators

import eu.geekhome.services.hardware.Temperature
import eu.geekhome.shellyplugin.TemperatureBriefDto

class ShellyTemperatureReadPortOperator(shellyId: String) : ShellyReadPortOperator<Temperature> {

    private val value = Temperature(0.0)
    override val readTopic = "shellies/$shellyId/sensor/temperature"

    override fun read(): Temperature {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueParsed = payload.toDouble()-273.15
        value.value = valueParsed
    }

    fun setValueFromTemperatureResponse(temperatureBrief: TemperatureBriefDto) {
        value.value = temperatureBrief.tC - 273.15
    }
}