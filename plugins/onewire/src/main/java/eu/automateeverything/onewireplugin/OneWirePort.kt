package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.adapter.DSPortAdapter
import com.dalsemi.onewire.container.Sleeper
import eu.automateeverything.domain.hardware.InputPort
import eu.automateeverything.domain.hardware.PortValue
import java.util.*

interface OneWirePort<V: PortValue> : InputPort<V> {
    val oneWireAddress: ByteArray
    fun refresh(now: Calendar, adapter: DSPortAdapter, sleeper: Sleeper)
}