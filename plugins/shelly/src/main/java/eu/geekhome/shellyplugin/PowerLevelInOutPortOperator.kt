package eu.geekhome.shellyplugin

import com.google.gson.Gson
import eu.geekhome.services.hardware.PowerLevel

class PowerLevelInOutPortOperator(
    shellyId: String,
    channel: Int,
) : ShellyInPortOperator<PowerLevel>, ShellyOutPortOperator<PowerLevel> {

    private val gson = Gson()
    private val value = PowerLevel(0)
    override val readTopic = "shellies/$shellyId/light/$channel/status"
    override val writeTopic = "shellies/$shellyId/light/$channel/set"

    override fun read(): PowerLevel {
        return value
    }

    override fun write(value: PowerLevel) {
        this.value.value = value.value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val response: LightResponseDto = gson.fromJson(payload, LightResponseDto::class.java)
        setValueFromLightResponse(response)
    }

    fun setValueFromLightResponse(lightResponse: LightResponseDto) {
        val valueInPercent = calculateBrightness(lightResponse)
        value.value = valueInPercent
    }

    private fun calculateBrightness(lightResponse: LightResponseDto): Int {
        val isOn = lightResponse.on
        return if (isOn) lightResponse.brightness else 0
    }

    override fun convertValueToMqttPayload(): String {
        val response: LightSetDto = if (value.value == 0) {
            LightSetDto("off", 0)
        } else {
            LightSetDto("off", value.value)
        }
        return gson.toJson(response)
    }

    override fun resetLatch() {
        value.resetLatch()
    }

    override fun isLatchTriggered(): Boolean {
        return value.isLatchTriggered
    }
}