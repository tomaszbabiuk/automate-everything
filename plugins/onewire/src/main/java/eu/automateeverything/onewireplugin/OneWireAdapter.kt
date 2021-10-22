package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.OneWireException
import com.dalsemi.onewire.adapter.DSPortAdapter
import com.dalsemi.onewire.adapter.USerialAdapter
import com.dalsemi.onewire.container.OneWireContainer28
import com.dalsemi.onewire.container.OneWireSensor
import com.dalsemi.onewire.container.TemperatureContainer
import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.hardware.HardwareAdapterBase
import eu.automateeverything.domain.hardware.Port
import eu.automateeverything.domain.hardware.PortIdBuilder
import eu.automateeverything.domain.hardware.Temperature
import kotlinx.coroutines.*
import java.util.*

class OneWireAdapter(
    owningPluginId: String,
    private val serialPortName: String)
    : HardwareAdapterBase<OneWirePort<*>>() {

    private var mapper: OneWireSensorToPortMapper
    private lateinit var adapter: USerialAdapter
    override val id: String = "1-WIRE $serialPortName"

    init {
        val portIdBuilder = PortIdBuilder(owningPluginId)
        mapper = OneWireSensorToPortMapper(portIdBuilder)
    }

    var operationScope: CoroutineScope? = null
    var operationSink: EventsSink? = null


    override fun executePendingChanges() {
        //TODO
    }

    @Throws(OneWireException::class)
    private fun initializeAdapter(port: String): USerialAdapter {
        val adapter = USerialAdapter()
        adapter.selectPort(port)
        adapter.reset()
        adapter.beginExclusive(true)
        adapter.setSearchAllDevices()
        adapter.targetAllFamilies()
        adapter.speed = DSPortAdapter.SPEED_REGULAR
        return adapter
    }

    private fun freeAdapter(adapter: USerialAdapter) {
        adapter.endExclusive()
        adapter.freePort()
    }

    override fun start(operationSink: EventsSink, settings: List<SettingsDto>) {
        this.operationSink = operationSink

        if (operationScope != null) {
            operationScope!!.cancel("Adapter already started")
        }

        adapter = initializeAdapter(serialPortName)
        searchForOneWireSensors(adapter)
            .map(mapper::map)
            .forEach { ports[it.id] = it }

        operationScope = CoroutineScope(Dispatchers.IO)
        operationScope?.launch {
            while (isActive) {
                maintenanceLoop(Calendar.getInstance())
                delay(10000)
            }
        }
    }

    private fun searchForOneWireSensors(adapter: USerialAdapter): List<OneWireSensor> {
        return adapter
            .allDeviceContainers
            .toList()
            .filterIsInstance<OneWireSensor>()
    }

    override fun stop() {
        operationScope?.cancel("Stop called")
        freeAdapter(adapter)
    }

    override suspend fun internalDiscovery(eventsSink: EventsSink): List<OneWirePort<*>> {
        println("one-wire discovery... returned already cached data")

        return ports.values.toList()
    }

    private suspend fun maintenanceLoop(now: Calendar) {
        ports
            .values
            .forEach {
                it.refresh()
            }
    }
}
