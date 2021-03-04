package eu.geekhome.shellyplugin

import eu.geekhome.services.hardware.Humidity

class ShellyHumidityReadPortOperator(shellyId: String) : ShellyReadPortOperator<Humidity> {

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