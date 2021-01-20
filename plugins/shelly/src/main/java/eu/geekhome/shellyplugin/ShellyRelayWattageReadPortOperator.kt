package eu.geekhome.shellyplugin

import eu.geekhome.services.hardware.Wattage

class ShellyRelayWattageReadPortOperator(shellyId: String,
                                         channel : Int,
) : ShellyReadPortOperator<Wattage> {

    private val value = Wattage(0.0)
    override val readTopic = "shellies/$shellyId/relay/$channel/power"

    override fun read(): Wattage {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueParsed = payload.toDouble()
        value.value = valueParsed
    }
}