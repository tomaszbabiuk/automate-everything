package eu.automateeverything.aforeplugin

import eu.automateeverything.domain.hardware.InputPort
import eu.automateeverything.domain.hardware.Port
import eu.automateeverything.domain.hardware.Wattage
import io.ktor.client.*
import io.ktor.client.request.*
import java.math.BigDecimal
import java.net.InetAddress
import java.util.*

class AforeWattageInputPort(
    override val id: String,
    private val httpClient: HttpClient,
    private val inetAddress: InetAddress
) : Port<Wattage>, InputPort<Wattage> {

    override val valueClazz = Wattage::class.java
    override var connectionValidUntil: Long = 0L

    init {
        prolongValidity(Calendar.getInstance())
    }

    private fun prolongValidity(now: Calendar) {
        connectionValidUntil = now.timeInMillis + 1000 * 60 * 5 //now + 5 minutes
    }

    suspend fun refresh(now: Calendar) {
        try {
            refreshInverterData()
            prolongValidity(now)
        } catch (ex: Exception) {
            markDisconnected()
        }
    }

    private var cachedValue = Wattage(BigDecimal.ZERO)

    override fun read(): Wattage {
        return cachedValue
    }

    private suspend fun refreshInverterData() {
        val newValue = readInverterPower()
        if (cachedValue.value != newValue) {
            cachedValue = Wattage(newValue)
        }
    }

    private suspend fun readInverterPower(): BigDecimal {
        val inverterResponse = httpClient.get<String>("http://$inetAddress/status.html")
        val lines = inverterResponse.split(";").toTypedArray()
        for (line in lines) {
            if (line.contains("webdata_now_p")) {
                val s = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""))
                return s.toBigDecimal()
            }
        }

        return BigDecimal.ZERO
    }
}