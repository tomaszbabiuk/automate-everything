package eu.geekhome.aforeplugin

import eu.geekhome.domain.hardware.InputPort
import eu.geekhome.domain.hardware.Port
import eu.geekhome.domain.hardware.Wattage
import io.ktor.client.*
import io.ktor.client.request.*
import java.net.InetAddress
import java.util.*

class AforeWattageInputPort(
    override val id: String,
    private val httpClient: HttpClient,
    private val inetAddress: InetAddress
) : Port<Wattage>, InputPort<Wattage> {

    override val valueType = Wattage::class.java
    override var connectionValidUntil: Long = 0L

    init {
        prolongValidity(Calendar.getInstance())
    }

    private fun prolongValidity(now: Calendar) {
        connectionValidUntil = now.timeInMillis + 1000 * 60 * 5 //now + 5 minutes
    }

    suspend fun refresh(now: Calendar): Boolean {
        try {
            val result = refreshInverterData()
            prolongValidity(now)
            return result
        } catch (ex: Exception) {
            markDisconnected()
        }

        return false
    }

    private var cachedValue = Wattage(0.0)

    override fun read(): Wattage {
        return cachedValue
    }

    private suspend fun refreshInverterData() : Boolean {
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