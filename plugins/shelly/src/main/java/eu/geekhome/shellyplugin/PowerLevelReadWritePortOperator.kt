package eu.geekhome.shellyplugin

import com.google.gson.Gson
import eu.geekhome.services.hardware.PowerLevel

class PowerLevelReadWritePortOperator(
    shellyId: String,
    channel: Int,
) : ShellyReadPortOperator<PowerLevel>, ShellyWritePortOperator<PowerLevel> {

    private val gson = Gson()
    private val readValue = PowerLevel(0)
    private var requestedValue : PowerLevel? = null
    override val readTopic = "shellies/$shellyId/light/$channel/status"
    override val writeTopic = "shellies/$shellyId/light/$channel/set"

    override fun read(): PowerLevel {
        return readValue
    }

//    override fun <PowerLevel> write(value: PowerLevel) {
//        requestedValue = value
//    }

    override fun write(value: PowerLevel) {
        requestedValue = value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val response: LightResponseDto = gson.fromJson(payload, LightResponseDto::class.java)
        setValueFromLightResponse(response)
    }

    fun setValueFromLightResponse(lightResponse: LightResponseDto) {
        val valueInPercent = calculateBrightness(lightResponse)
        readValue.value = valueInPercent
    }

    private fun calculateBrightness(lightResponse: LightResponseDto): Int {
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