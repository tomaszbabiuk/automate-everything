package eu.geekhome.aforeplugin

import eu.geekhome.domain.hardware.ReadPortOperator
import eu.geekhome.domain.hardware.Wattage
import io.ktor.client.*
import io.ktor.client.request.*
import java.net.InetAddress

class AforeWattageReadPortOperator(
    private val httpClient: HttpClient,
    private val inetAddress: InetAddress,
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
        val inverterResponse = httpClient.get<String>("http://$inetAddress/status.html")
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