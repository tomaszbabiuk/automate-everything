package eu.automateeverything.shellyplugin.ports

import eu.automateeverything.domain.hardware.BinaryInput
import eu.automateeverything.shellyplugin.StateBriefDto

class ShellyStateInputPort(
    id: String,
    shellyId: String,
    sleepInterval: Long)
    : ShellyInputPort<BinaryInput>(id, BinaryInput::class.java, sleepInterval) {

    private val value = BinaryInput(false)
    override val readTopic = "shellies/$shellyId/sensor/state"

    override fun read(): BinaryInput {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        value.value = payload == "open"
    }

    fun setValueFromStateResponse(stateBrief: StateBriefDto) {
        value.value = stateBrief.state == "open"
    }
}