package eu.geekhome.shellyplugin.ports

import eu.geekhome.domain.hardware.Relay
import eu.geekhome.shellyplugin.RelayResponseDto

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
        val valueAsBoolean = payload == "on"
        readValue.value = valueAsBoolean
    }

    override fun getExecutePayload(): String? {
        if (requestedValue == null) {
            return null
        }

        return if (requestedValue!!.value) "on" else "off"
    }

    fun setValueFromRelayResponse(response: RelayResponseDto) {
        readValue.value = response.ison
    }

    override fun reset() {
        requestedValue = null
    }
}