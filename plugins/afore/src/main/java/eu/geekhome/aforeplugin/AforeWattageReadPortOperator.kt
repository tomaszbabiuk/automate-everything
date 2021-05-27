package eu.geekhome.aforeplugin

import eu.geekhome.aforeplugin.AforeAdapter.Companion.INVERTER_IP_ADDRESS
import eu.geekhome.services.hardware.ReadPortOperator
import eu.geekhome.services.hardware.Wattage
import io.ktor.client.*
import io.ktor.client.request.*

class AforeWattageReadPortOperator(
    private val httpClient: HttpClient
) : ReadPortOperator<Wattage> {

    private var cachedValue = Wattage(0.0)

    override fun read(): Wattage {
        return cachedValue
    }

    suspend fun refresh() : Boolean {
        val newValue = readInverterPower()
        if (cachedValue.value != newValue) {
            cachedValue = Wattage(newValue)
            return true
        }

        return false
    }

    private suspend fun readInverterPower(): Double {
        val inverterResponse = httpClient.get<String>("http://$INVERTER_IP_ADDRESS/status.html")
        val lines = inverterResponse.split(";").toTypedArray()
        for (line in lines) {
            if (line.contains("webdata_now_p")) {
                val s = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""))
                return java.lang.Double.valueOf(s)
            }
        }

        return 0.0

    }

}