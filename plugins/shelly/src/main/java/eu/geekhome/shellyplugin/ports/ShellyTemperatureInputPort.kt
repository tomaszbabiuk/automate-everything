package eu.geekhome.shellyplugin.ports

import eu.geekhome.domain.hardware.Temperature
import eu.geekhome.shellyplugin.TemperatureBriefDto

class ShellyTemperatureInputPort(
    id: String,
    shellyId: String,
    sleepInterval: Long)
    : ShellyInputPort<Temperature>(id, Temperature::class.java, sleepInterval) {

    private val value = Temperature(0.0)
    override val readTopic = "shellies/$shellyId/temperature"

    override fun read(): Temperature {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueParsed = payload.toDouble() + 273.15
        value.value = valueParsed
    }

    fun setValueFromTemperatureResponse(temperatureBrief: TemperatureBriefDto) {
        value.value = temperatureBrief.tC + 273.15
    }
}