package eu.geekhome.shellyplugin

import eu.geekhome.services.hardware.Relay

class ShellyRelayReadWritePortOperator(
    shellyId: String,
    channel: Int,
) : ShellyReadPortOperator<Relay>, ShellyWritePortOperator<Relay> {

    private val readValue = Relay(false)
    private var requestedValue : Relay? = null
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