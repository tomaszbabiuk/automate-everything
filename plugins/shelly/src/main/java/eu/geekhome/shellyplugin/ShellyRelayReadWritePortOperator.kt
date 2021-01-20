package eu.geekhome.shellyplugin

import eu.geekhome.services.hardware.Relay

class ShellyRelayReadWritePortOperator(
    shellyId: String,
    channel: Int,
) : ShellyReadPortOperator<Relay>, ShellyWritePortOperator<Relay> {

    private val value = Relay(false)
    override val readTopic = "shellies/$shellyId/relay/$channel"
    override val writeTopic = "shellies/$shellyId/relay/$channel/command"

    override fun read(): Relay {
        return value
    }

    override fun write(value: Relay) {
        this.value.value = value.value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueAsBoolean = payload == "on"
        value.value = valueAsBoolean
    }

    override fun convertValueToMqttPayload(): String {
        return if (value.value) "on" else "off"
    }

    override fun resetLatch() {
        value.resetLatch()
    }

    override fun isLatchTriggered(): Boolean {
        return value.isLatchTriggered
    }

    fun setValueFromRelayResponse(response: RelayResponseDto) {
        value.value = response.on
    }
}