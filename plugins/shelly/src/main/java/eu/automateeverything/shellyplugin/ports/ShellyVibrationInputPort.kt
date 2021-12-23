package eu.automateeverything.shellyplugin.ports

import eu.automateeverything.domain.hardware.BinaryInput
import eu.automateeverything.shellyplugin.AccelBriefDto
import eu.automateeverything.shellyplugin.StateBriefDto

class ShellyVibrationInputPort(
    id: String,
    shellyId: String,
    sleepInterval: Long)
    : ShellyInputPort<BinaryInput>(id, BinaryInput::class.java, sleepInterval) {

    private val value = BinaryInput(false)
    override val readTopic = "shellies/$shellyId/sensor/vibration"

    override fun read(): BinaryInput {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        value.value = payload == "1"
    }

    fun setValueFromAccelResponse(stateBrief: AccelBriefDto) {
        value.value = stateBrief.vibration == 1
    }
}