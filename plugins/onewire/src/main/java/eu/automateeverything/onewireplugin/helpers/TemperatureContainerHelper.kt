package eu.automateeverything.onewireplugin.helpers

import com.dalsemi.onewire.adapter.DSPortAdapter
import com.dalsemi.onewire.container.OneWireContainer28
import com.dalsemi.onewire.container.Sleeper
import com.dalsemi.onewire.container.ThreadSleeper
import java.math.BigDecimal
import java.math.RoundingMode

object TemperatureContainerHelper {

    private fun readTemperature(adapter: DSPortAdapter, address: ByteArray, sleeper: Sleeper): Double {
        val container = OneWireContainer28(adapter, address)
        var state: ByteArray = container.readDevice()
        container.doTemperatureConvert(state, sleeper)
        state = container.readDevice()
        val lastTemp: BigDecimal = BigDecimal(container.getTemperature(state)).setScale(2, RoundingMode.CEILING)
        return lastTemp.toDouble()
    }

    fun read(adapter: DSPortAdapter, address: ByteArray) : Double {
        return readTemperature(adapter, address, ThreadSleeper())
    }
}