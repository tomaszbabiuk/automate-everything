package eu.automateeverything.onewireplugin.helpers

import com.dalsemi.onewire.OneWireException
import com.dalsemi.onewire.adapter.DSPortAdapter
import com.dalsemi.onewire.container.OneWireContainer29
import com.dalsemi.onewire.container.Sleeper
import com.dalsemi.onewire.container.SwitchContainer

object SwitchContainerHelper {

    fun readChannelsCount(adapter: DSPortAdapter, address: ByteArray): Int {
        return try {
            val container = OneWireContainer29(adapter, address)
            val registersData = container.readDevice()
            container.getNumberChannels(registersData)
        } catch (ex: java.lang.Exception) {
            0
        }
    }

    @Throws(OneWireException::class)
    fun execute(switchContainer: SwitchContainer, sleeper: Sleeper) {
        var trial = 1
        while (trial < 4) {
            if (executeWithBackoff(switchContainer, sleeper)) {
                return
            }
            trial++
        }
    }

    private fun executeWithBackoff(switchContainer: SwitchContainer, sleeper: Sleeper): Boolean {
        return try {
            executeInternal(switchContainer)
            true
        } catch (ex: Exception) {
            sleeper.sleep(500)
            false
        }
    }

    @Throws(OneWireException::class)
    private fun executeInternal(switchContainer: SwitchContainer) {
//        val prevValuesSnapshot: BooleanArray = _prevValues.clone()
//        if (!Arrays.equals(_values, _prevValues)) {
//            makeCopy()
//            for (i in _values.indices) {
//                if (_values.get(i) != prevValuesSnapshot[i]) {
//                    _logger.activity(getAddressAsString().toString() + "-" + i, _values.get(i), prevValuesSnapshot[i])
//                    val data: ByteArray = _wrapped.readDevice()
//                    _wrapped.setLatchState(i, !_values.get(i), false, data)
//                    _wrapped.writeDevice(data)
//                }
//            }
//        }
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