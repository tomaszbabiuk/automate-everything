package eu.automateeverything.shellyplugin.ports

import eu.automateeverything.domain.hardware.Temperature
import eu.automateeverything.shellyplugin.TemperatureBriefDto
import java.math.BigDecimal

class ShellyTemperatureInputPort(
    id: String,
    shellyId: String,
    sleepInterval: Long)
    : ShellyInputPort<Temperature>(id, Temperature::class.java, sleepInterval) {

    private val value = Temperature(BigDecimal.ZERO)
    override val readTopic = "shellies/$shellyId/temperature"

    override fun read(): Temperature {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueParsed = payload.toBigDecimal() + 273.15.toBigDecimal()
        value.value = valueParsed
    }

    fun setValueFromTemperatureResponse(temperatureBrief: TemperatureBriefDto) {
        value.value = temperatureBrief.tC + 273.15.toBigDecimal()
    }
}