package eu.automateeverything.shellyplugin.ports

import eu.automateeverything.domain.hardware.Wattage
import java.math.BigDecimal

enum class TopicSource {
    Light,
    Relay
}

class ShellyWattageInputPort(
    id: String,
    shellyId: String,
    channel: Int,
    sleepInterval: Long,
    topicSource: TopicSource,
) : ShellyInputPort<Wattage>(id, Wattage::class.java, sleepInterval) {

    private val value = Wattage(BigDecimal.ZERO)

    override val readTopic =
        if (topicSource == TopicSource.Relay) {
            "shellies/$shellyId/relay/$channel/power"
        } else {
            "shellies/$shellyId/light/$channel/power"
        }

    override fun read(): Wattage {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueParsed = payload.toBigDecimal()
        value.value = valueParsed
    }
}