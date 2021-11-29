package eu.automateeverything.shellyplugin.ports

import eu.automateeverything.domain.hardware.BatteryCharge
import eu.automateeverything.shellyplugin.BatteryBriefDto
import java.math.BigDecimal

class ShellyBatteryInputPort(id: String, shellyId: String, sleepInterval: Long) :
    ShellyInputPort<BatteryCharge>(id, BatteryCharge::class.java, sleepInterval) {

    private val value = BatteryCharge(BigDecimal.ZERO)
    override val readTopic = "shellies/$shellyId/sensor/battery"

    override fun read(): BatteryCharge {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueParsed = payload.toBigDecimal()
        value.value = valueParsed
    }

    fun setValueFromBatteryResponse(batteryBrief: BatteryBriefDto) {
        value.value = batteryBrief.value
    }
}