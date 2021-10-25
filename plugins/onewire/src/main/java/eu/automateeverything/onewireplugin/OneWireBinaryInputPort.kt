package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.adapter.DSPortAdapter
import com.dalsemi.onewire.container.Sleeper
import eu.automateeverything.domain.hardware.BinaryInput
import java.util.*

class OneWireBinaryInputPort(
    override val id: String,
    val channel: Int,
    override val oneWireAddress: ByteArray,
    initialReading: Boolean,
) : OneWirePort<BinaryInput> {

    override var connectionValidUntil = Long.MAX_VALUE
    override val valueClazz = BinaryInput::class.java
    private var lastTemperatureRead: Long
    private var lastTemperature: BinaryInput

    init {
        val now = Calendar.getInstance()
        lastTemperatureRead = now.timeInMillis
        lastTemperature = BinaryInput(initialReading)
    }

    override fun read(): BinaryInput {
        return lastTemperature
    }

    override fun refresh(now: Calendar, adapter: DSPortAdapter, sleeper: Sleeper) {
    }
}