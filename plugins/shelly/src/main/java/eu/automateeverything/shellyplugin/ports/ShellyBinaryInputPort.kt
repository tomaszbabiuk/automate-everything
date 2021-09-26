package eu.automateeverything.shellyplugin.ports

import eu.automateeverything.domain.hardware.BinaryInput
import eu.automateeverything.shellyplugin.InputBriefDto

class ShellyBinaryInputPort(
    id: String,
    shellyId: String,
    channel: Int,
    sleepInterval: Long)
    : ShellyInputPort<BinaryInput>(id, BinaryInput::class.java, sleepInterval) {

    private val value = BinaryInput(false)
    override val readTopic = "shellies/$shellyId/input/$channel"

    override fun read(): BinaryInput {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        value.value = payload == "1"
    }

    fun setValueFromInputResponse(inputBrief: InputBriefDto) {
        value.value = inputBrief.input == 1
    }
}