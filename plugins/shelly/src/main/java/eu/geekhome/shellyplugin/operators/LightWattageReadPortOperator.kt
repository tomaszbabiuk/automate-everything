package eu.geekhome.shellyplugin.operators

import eu.geekhome.domain.hardware.Wattage

class LightWattageReadPortOperator(shellyId: String,
                                   channel : Int,
) : ShellyReadPortOperator<Wattage> {

    private val value = Wattage(0.0)
    override val readTopic = "shellies/$shellyId/light/$channel/power"

    override fun read(): Wattage {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueParsed = payload.toDouble()
        value.value = valueParsed
    }
}