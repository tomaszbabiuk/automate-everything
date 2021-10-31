package eu.automateeverything.shellyplugin.ports

import eu.automateeverything.domain.hardware.BatteryCharge
import eu.automateeverything.shellyplugin.BatteryBriefDto

class ShellyBatteryInputPort(id: String, shellyId: String, sleepInterval: Long) :
    ShellyInputPort<BatteryCharge>(id, BatteryCharge::class.java, sleepInterval) {

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