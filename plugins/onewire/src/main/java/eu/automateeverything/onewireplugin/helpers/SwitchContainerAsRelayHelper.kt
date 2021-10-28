package eu.automateeverything.onewireplugin.helpers

import com.dalsemi.onewire.container.SwitchContainer
import kotlin.Throws
import com.dalsemi.onewire.OneWireException
import java.lang.Exception

object SwitchContainerAsRelayHelper {



    fun readChannelsCount(switchContainer: SwitchContainer): Int {
        return try {
            switchContainer.getNumberChannels(switchContainer.readDevice())
        } catch (ex: Exception) {
            0
        }
    }

    @Throws(OneWireException::class)
    fun read(switchContainer: SwitchContainer, sensing: Boolean): Array<SwitchContainerReading> {
        return try {
            val readings = readInternal(switchContainer, sensing, false)
            throwIfAllReadingsAreSensed(readings)
            readings
        } catch (ex: Exception) {
            val readings = readInternal(switchContainer, sensing, true)
            throwIfAllReadingsAreSensed(readings)
            readings
        }
    }

    @Throws(OneWireException::class)
    private fun throwIfAllReadingsAreSensed(readings: Array<SwitchContainerReading>) {
        for (reading in readings) {
            if (!reading.isSensed) {
                return
            }
        }

        throw OneWireException("All readings are SENSED! It's impossible unless there's an error on one-wire line. Ignoring...")
    }

    @Throws(OneWireException::class)
    private fun readInternal(
        switchContainer: SwitchContainer,
        sensing: Boolean,
        lastReadingHasFailed: Boolean
    ): Array<SwitchContainerReading> {
        val state = switchContainer.readDevice()
        val channelsCount = switchContainer.getNumberChannels(state)
        val result = Array(channelsCount) {
            SwitchContainerReading(
                level = false,
                isSensed = false
            )
        }
        if (sensing) {
            for (channel in 0 until channelsCount) {
                switchContainer.setLatchState(channel, true, false, state)
            }
            switchContainer.writeDevice(state)
            for (channel in 0 until channelsCount) {
                val level = switchContainer.getLevel(channel, state)
                //Last reading has failed?... disable sensing in this round!
                val sensed = !lastReadingHasFailed && switchContainer.getSensedActivity(channel, state)
                switchContainer.setLatchState(channel, !level, false, state)
                result[channel] = SwitchContainerReading(level, sensed)
            }
            switchContainer.clearActivity()
        } else {
            for (channel in 0 until channelsCount) {
                val level = switchContainer.getLevel(channel, state)
                result[channel] = SwitchContainerReading(level, false)
            }
        }
        return result
    }

    data class SwitchContainerReading(val level: Boolean, val isSensed: Boolean)
}