package eu.geekhome.shellyplugin.ports

import eu.geekhome.domain.hardware.Humidity
import eu.geekhome.shellyplugin.HumidityBriefDto

class ShellyHumidityInputPort(
        id: String,
        shellyId: String,
        sleepInterval: Long) : ShellyInputPort<Humidity>(id, Humidity::class.java, sleepInterval) {

    private val value = Humidity(0.0)
    override val readTopic = "shellies/$shellyId/sensor/humidity"

    override fun read(): Humidity {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueParsed = payload.toDouble()
        value.value = valueParsed
    }

    fun setValueFromHumidityResponse(humidityBrief: HumidityBriefDto) {
        value.value = humidityBrief.value
    }
}