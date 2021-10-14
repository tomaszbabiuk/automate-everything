package eu.automateeverything.onewireplugin

import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.hardware.*
import gnu.io.CommPortIdentifier

class OneWireAdapter(
    private val owningPluginId: String,
    private val serialPort: String)
    : HardwareAdapterBase<OneWirePort<*>>() {

    override val id: String = "1-WIRE $serialPort"

    override fun executePendingChanges() {
        println("executing pending changes")
    }

    override fun stop() {
        println("stopping 1-wire")
    }

    override fun start(operationSink: EventsSink, settings: List<SettingsDto>) {
        println("starting 1-wire")
    }

    override suspend fun internalDiscovery(eventsSink: EventsSink): List<OneWirePort<*>> {
        println("internal discovery")

        val ports = CommPortIdentifier.getPortIdentifiers()
        val discoveryProcess = DiscoveryProcess(serialPort)
        try {
            val devicesFound = discoveryProcess.call()
            println(devicesFound)
        }
        catch (ex: Exception) {
            eventsSink.broadcastDiscoveryEvent(owningPluginId, ex.message!!)
        }

        return listOf()
    }
}

open class OneWirePort<V: PortValue>(
    override val id : String,
    override val valueClazz: Class<V>
) : Port<V> {
    override var connectionValidUntil: Long = Long.MAX_VALUE
}