package eu.automateeverything.shellyplugin.ports

import eu.automateeverything.domain.hardware.Relay
import eu.automateeverything.shellyplugin.RelayResponseDto
import java.math.BigDecimal

class ShellyRelayOutputPort(
    id: String,
    shellyId: String,
    channel: Int,
    sleepInterval: Long
) : ShellyOutputPort<Relay>(id, Relay::class.java, sleepInterval) {

    private val readValue = Relay(false)
    override var requestedValue : Relay? = null
    override val readTopic = "shellies/$shellyId/relay/$channel"
    override val writeTopic = "shellies/$shellyId/relay/$channel/command"

    override fun read(): Relay {
        if (requestedValue != null) {
            return requestedValue!!
        }
        return readValue
    }

    override fun write(value: Relay) {
        this.requestedValue = value
    }

    override fun setValueFromMqttPayload(payload: String) {
        readValue.value = if (payload == "on") BigDecimal.ONE else BigDecimal.ZERO
    }

    override fun getExecutePayload(): String? {
        if (requestedValue == null) {
            return null
        }

        return if (requestedValue!!.value == BigDecimal.ONE) "on" else "off"
    }

    fun setValueFromRelayResponse(response: RelayResponseDto) {
        readValue.value = if (response.ison) BigDecimal.ONE else BigDecimal.ZERO
    }

    override fun reset() {
        requestedValue = null
    }
}