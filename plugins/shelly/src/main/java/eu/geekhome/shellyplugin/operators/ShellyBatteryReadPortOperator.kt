package eu.geekhome.shellyplugin.operators

import eu.geekhome.domain.hardware.BatteryCharge
import eu.geekhome.shellyplugin.BatteryBriefDto

class ShellyBatteryReadPortOperator(shellyId: String) : ShellyReadPortOperator<BatteryCharge> {

    private val value = BatteryCharge(0.0)
    override val readTopic = "shellies/$shellyId/sensor/battery"

    override fun read(): BatteryCharge {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueParsed = payload.toDouble()
        value.value = valueParsed
    }

    fun setValueFromBatteryResponse(batteryBrief: BatteryBriefDto) {
        value.value = batteryBrief.value
    }
}