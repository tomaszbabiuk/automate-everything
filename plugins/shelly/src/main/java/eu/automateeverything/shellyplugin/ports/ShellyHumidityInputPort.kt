package eu.automateeverything.shellyplugin.ports

import eu.automateeverything.domain.hardware.Humidity
import eu.automateeverything.shellyplugin.HumidityBriefDto
import java.math.BigDecimal

class ShellyHumidityInputPort(
        id: String,
        shellyId: String,
        sleepInterval: Long) : ShellyInputPort<Humidity>(id, Humidity::class.java, sleepInterval) {

    private val value = Humidity(BigDecimal.ZERO)
    override val readTopic = "shellies/$shellyId/sensor/humidity"

    override fun read(): Humidity {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueParsed = payload.toBigDecimal()
        value.value = valueParsed
    }

    fun setValueFromHumidityResponse(humidityBrief: HumidityBriefDto) {
        value.value = humidityBrief.value
    }
}