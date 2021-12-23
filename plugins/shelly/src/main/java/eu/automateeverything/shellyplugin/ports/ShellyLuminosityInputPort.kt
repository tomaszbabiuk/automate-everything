package eu.automateeverything.shellyplugin.ports

import eu.automateeverything.domain.hardware.Luminosity
import eu.automateeverything.shellyplugin.LuminosityBriefDto
import java.math.BigDecimal

class ShellyLuminosityInputPort(
        id: String,
        shellyId: String,
        sleepInterval: Long) : ShellyInputPort<Luminosity>(id, Luminosity::class.java, sleepInterval) {

    private val value = Luminosity(BigDecimal.ZERO)
    override val readTopic = "shellies/$shellyId/sensor/lux"

    override fun read(): Luminosity {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueParsed = payload.toBigDecimal()
        value.value = valueParsed
    }

    fun setValueFromLuminosityResponse(luminosityBrief: LuminosityBriefDto) {
        value.value = luminosityBrief.value
    }
}