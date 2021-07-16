package eu.geekhome.shellyplugin.ports

import com.google.gson.Gson
import eu.geekhome.domain.hardware.PowerLevel
import eu.geekhome.shellyplugin.LightBriefDto
import eu.geekhome.shellyplugin.LightSetDto

class ShellyPowerLevelOutputPort(
    id: String,
    shellyId: String,
    channel: Int,
    sleepInterval: Long
) : ShellyOutputPort<PowerLevel>(id, PowerLevel::class.java, sleepInterval) {

    private val gson = Gson()
    private val readValue = PowerLevel(0)
    private var requestedValue : PowerLevel? = null
    override val readTopic = "shellies/$shellyId/light/$channel/status"
    override val writeTopic = "shellies/$shellyId/light/$channel/set"

    override fun read(): PowerLevel {
        return readValue
    }

    override fun write(value: PowerLevel) {
        requestedValue = value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val response: LightBriefDto = gson.fromJson(payload, LightBriefDto::class.java)
        setValueFromLightResponse(response)
    }

    fun setValueFromLightResponse(lightResponse: LightBriefDto) {
        val valueInPercent = calculateBrightness(lightResponse)
        readValue.value = valueInPercent
    }

    private fun calculateBrightness(lightResponse: LightBriefDto): Int {
        val isOn = lightResponse.ison
        return if (isOn) lightResponse.brightness else 0
    }

    override fun getExecutePayload(): String? {
        if (requestedValue == null) {
            return null
        }

        val response: LightSetDto = if (requestedValue!!.value == 0) {
            LightSetDto("off", 0)
        } else {
            LightSetDto("on", requestedValue!!.value)
        }
        return gson.toJson(response)
    }


    override fun reset() {
        requestedValue = null
    }
}