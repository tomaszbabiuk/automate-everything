package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.OneWireException
import com.dalsemi.onewire.adapter.DSPortAdapter
import com.dalsemi.onewire.container.OneWireContainer28
import eu.automateeverything.domain.hardware.InputPort
import eu.automateeverything.domain.hardware.PortValue
import eu.automateeverything.domain.hardware.Temperature
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

interface OneWirePort<V: PortValue> : InputPort<V> {
    fun refresh(now: Calendar, adapter: DSPortAdapter)
}

class OneWireTemperatureInputPort(
    override val id: String,
    private val oneWireAddress: ByteArray,
    temporaryAdapter: DSPortAdapter
) : OneWirePort<Temperature> {

    override var connectionValidUntil = Long.MAX_VALUE
    override val valueClazz = Temperature::class.java
    private var lastTemperatureRead: Long
    private var lastTemperature: Temperature

    init {
        val now = Calendar.getInstance()
        lastTemperatureRead = now.timeInMillis

        try {
            lastTemperature = Temperature(readTemperatureInternal(now, temporaryAdapter) + 273.15)
        } catch (ex: Exception) {
            lastTemperature = Temperature(Double.NaN)
            markDisconnected()
        }
    }

    override fun read(): Temperature {
        return lastTemperature
    }

    override fun refresh(now: Calendar, adapter: DSPortAdapter) {
        val newTemperature = tryReadTemperature(now, adapter)
        if (newTemperature != null) {
            lastTemperature = Temperature(newTemperature + 273.15)
        }
    }

    @Throws(AdapterNonOperationalException::class)
    fun tryReadTemperature(now: Calendar, adapter: DSPortAdapter): Double? {
        val nowInMillis =  now.timeInMillis
        return if (nowInMillis - lastTemperatureRead < 30000) {
            lastTemperature.value - 273.15
        } else try {
            if (nowInMillis - lastTemperatureRead > 120000) {
                throw AdapterNonOperationalException()
            }

            readTemperatureInternal(now, adapter)
        } catch (ex: OneWireException) {
            markDisconnected()
            null
        }
    }

    @Throws(OneWireException::class)
    private fun readTemperatureInternal(now: Calendar, adapter: DSPortAdapter): Double {
        val container = OneWireContainer28(adapter, oneWireAddress)
        var state: ByteArray = container.readDevice()
        container.doTemperatureConvert(state)
        state = container.readDevice()
        lastTemperatureRead = now.timeInMillis
        val lastTemp: BigDecimal = BigDecimal(container.getTemperature(state)).setScale(2, RoundingMode.CEILING)
        connectionValidUntil = Long.MAX_VALUE
        return lastTemp.toDouble()
    }
}