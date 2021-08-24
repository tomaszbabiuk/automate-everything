package eu.automateeverything.crypto.binance

import eu.geekhome.data.settings.SettingsDto
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.hardware.HardwareAdapterBase
import eu.geekhome.domain.hardware.Port

class BinanceCryptoHardwareAdapter : HardwareAdapterBase<Port<*>>() {
    override fun executePendingChanges() {
    }

    override fun stop() {
    }

    override fun start(operationSink: EventsSink, settings: List<SettingsDto>) {
    }

    override suspend fun internalDiscovery(eventsSink: EventsSink): List<Port<*>> {
        return listOf()
    }
}