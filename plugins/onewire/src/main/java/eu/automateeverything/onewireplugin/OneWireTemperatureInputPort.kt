package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.OneWireException
import com.dalsemi.onewire.container.OneWireContainer28
import eu.automateeverything.domain.hardware.InputPort
import eu.automateeverything.domain.hardware.Port
import eu.automateeverything.domain.hardware.PortValue
import eu.automateeverything.domain.hardware.Temperature
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

interface OneWirePort<V: PortValue> : Port<V> {
    fun refresh()
}

class OneWireTemperatureInputPort(
    override val id: String,
    private val container: OneWireContainer28,
) : InputPort<Temperature>, OneWirePort<Temperature> {

    override var connectionValidUntil = Long.MAX_VALUE
    override val valueClazz = Temperature::class.java
    private var lastTemperatureRead = Calendar.getInstance().timeInMillis
    private var lastTemperature: Temperature

    init {
        try {
            lastTemperature = Temperature(readTemperatureInternal() + 273.15)
        } catch (ex: Exception) {
            lastTemperature = Temperature(Double.NaN)
            markDisconnected()
        }
    }

    override fun read(): Temperature {
        return lastTemperature
    }

    override fun refresh() {
        val newTemperature = tryReadTemperature()
        if (newTemperature != null) {
            lastTemperature = Temperature(newTemperature + 273.15)
        }
    }

    @Throws(AdapterNonOperationalException::class)
    fun tryReadTemperature(): Double? {
        val now = Calendar.getInstance().timeInMillis
        return if (now - lastTemperatureRead < 30000) {
            lastTemperature.value - 273.15
        } else try {
            if (now - lastTemperatureRead > 120000) {
                throw AdapterNonOperationalException()
            }

            readTemperatureInternal()
        } catch (ex: OneWireException) {
            markDisconnected()
            null
        }
    }

    @Throws(OneWireException::class)
    private fun readTemperatureInternal(): Double {
        var state: ByteArray = container.readDevice()
        container.doTemperatureConvert(state)
        state = container.readDevice()
        lastTemperatureRead = Calendar.getInstance().timeInMillis
        val lastTemp: BigDecimal = BigDecimal(container.getTemperature(state)).setScale(2, RoundingMode.CEILING)
        connectionValidUntil = Long.MAX_VALUE
        return lastTemp.toDouble()
    }
}