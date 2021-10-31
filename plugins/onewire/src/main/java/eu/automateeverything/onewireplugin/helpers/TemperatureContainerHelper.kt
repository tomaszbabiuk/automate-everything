package eu.automateeverything.onewireplugin.helpers

import com.dalsemi.onewire.container.OneWireContainer28
import com.dalsemi.onewire.container.Sleeper
import com.dalsemi.onewire.container.TemperatureContainer
import com.dalsemi.onewire.container.ThreadSleeper
import java.math.BigDecimal
import java.math.RoundingMode

object TemperatureContainerHelper {

    private fun readTemperature(temperatureContainer: TemperatureContainer, sleeper: Sleeper): Double {
        var state: ByteArray = temperatureContainer.readDevice()
        if (temperatureContainer is OneWireContainer28) {
            temperatureContainer.doTemperatureConvert(state, sleeper)
        }
        else {
            temperatureContainer.doTemperatureConvert(state)
        }
        state = temperatureContainer.readDevice()
        val lastTemp: BigDecimal = BigDecimal(temperatureContainer.getTemperature(state)).setScale(3, RoundingMode.CEILING)
        return lastTemp.toDouble()
    }

    fun read(temperatureContainer: TemperatureContainer) : Double {
        return readTemperature(temperatureContainer, ThreadSleeper())
    }
}