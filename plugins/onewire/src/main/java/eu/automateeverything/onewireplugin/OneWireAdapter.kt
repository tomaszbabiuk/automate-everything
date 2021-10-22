package eu.automateeverything.onewireplugin

import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.hardware.*
import kotlinx.coroutines.*
import java.util.*

class OneWireAdapter(
    private val owningPluginId: String,
    private val serialPort: String)
    : HardwareAdapterBase<Port<*>>() {

    override val id: String = "1-WIRE $serialPort"
    private val portIdBuilder = PortIdBuilder(owningPluginId)

    private  val  _pipe: MutableList<AdapterTask> = ArrayList()
    private  val  _switchesAsSensedInputs = ArrayList<SwitchDiscoveryInfo>()
    private  val  _switchesAsInputs = ArrayList<SwitchDiscoveryInfo>()
    private  val  _switchesAsOutputs = ArrayList<SwitchDiscoveryInfo>()
    private  val  _allSwitches: MutableList<SwitchDiscoveryInfo> = ArrayList()
    private  val  _identifiers: MutableList<IdentityDiscoveryInfo> = ArrayList()
    var operationScope: CoroutineScope? = null
    var operationSink: EventsSink? = null


    override fun executePendingChanges() {
        _allSwitches.forEach {
            it.container.tryExecute()
        }
    }

    override fun stop() {
        operationScope?.cancel("Stop called")
    }

    override fun start(operationSink: EventsSink, settings: List<SettingsDto>) {
        this.operationSink = operationSink

        if (operationScope != null) {
            operationScope!!.cancel("Adapter already started")
        }

        operationScope = CoroutineScope(Dispatchers.IO)
        operationScope?.launch {
            while (isActive) {
                maintenanceLoop(Calendar.getInstance())
                delay(1000)
            }
        }
    }

    override suspend fun internalDiscovery(eventsSink: EventsSink): List<Port<*>> {
        println("internal discovery")

        val discoveryProcess = DiscoveryProcess(serialPort)

        var hasThermometers = false
        var hasSwitches = false

        try {
            for (di in discoveryProcess.call()) {
                if (di is TemperatureDiscoveryInfo) {
                    hasThermometers = true
                    val inputPortId = portIdBuilder.buildPortId(di.address, 0, "I")
                    val inputPort = OneWireTemperatureInputPort(inputPortId, Temperature(di.initialTemperature))
                    ports[inputPortId] = inputPort
                } else if (di is SwitchDiscoveryInfo) {
                    hasSwitches = true
                    _allSwitches.add(di)
                    for (channel in 0 until di.channelsCount) {
                        //TODO: read initial value
                        val inputPortId = portIdBuilder.buildPortId(di.address, channel, "I")
                        val inputPort = OneWireBinaryInputPort(inputPortId, BinaryInput(false))
                        ports[inputPortId] = inputPort

                        //TODO: read initial value
                        val outputPortId = portIdBuilder.buildPortId(di.address, channel, "R")
                        val outputPort = OneWireRelayOutputPort(inputPortId, di.container, channel)
                        ports[outputPortId] = outputPort
                    }
                } else if (di is IdentityDiscoveryInfo) {
                    _identifiers.add(di)
                }
            }
        } catch (ex: Exception) {
            eventsSink.broadcastDiscoveryEvent(owningPluginId, ex.message!!)
        }

        if (hasSwitches && !hasThermometers) {
            _pipe.add(AdapterTask(TaskType.Continue, null))
        }

        return listOf()
    }

    private suspend fun maintenanceLoop(now: Calendar) {
            val tasks = ArrayList<AdapterTask>()
            if (_switchesAsSensedInputs.size > 0 || _switchesAsOutputs.size > 0) {
                for (di in _switchesAsOutputs) {
                    tasks.add(AdapterTask(TaskType.WriteSwitchValue, di))
                }
                for (di in _switchesAsSensedInputs) {
                    tasks.add(AdapterTask(TaskType.ReadSensedSwitchValue, di))
                }
                for (di in _switchesAsInputs) {
                    tasks.add(AdapterTask(TaskType.ReadSwitchValue, di))
                }
                for (di in _identifiers) {
                    tasks.add(AdapterTask(TaskType.ValidateIdentity, di))
                }
            }
            tasks.add(AdapterTask(TaskType.RefreshLoopFinished, null))
            _pipe.addAll(if (_pipe.size > 0) 0 else 0, tasks)

        val refreshProcess = RefreshProcess(serialPort, _pipe) { hasRefreshErrors ->
            //_state = if (hasRefreshErrors) RefreshState.RefreshError else RefreshState.RefreshSuccess
            //TODO: log refreshed?
        }

        refreshProcess.run()
    }
}

class OneWireBinaryInputPort(
    override val id: String,
    private val initialValue: BinaryInput)
    : InputPort<BinaryInput> {

    override var connectionValidUntil = Long.MAX_VALUE

    override val valueClazz = BinaryInput::class.java

    override fun read(): BinaryInput {
        return initialValue
    }
}

class OneWireRelayOutputPort(
    override val id: String,
    private var switchContainer: SwitchContainerWrapper,
    private val channel: Int            )
    : OutputPort<Relay> {

    override var connectionValidUntil = Long.MAX_VALUE

    override val valueClazz = Relay::class.java
    override var requestedValue : Relay? = null

    override fun read(): Relay {
        return Relay(switchContainer.read(channel))
    }

    override fun write(value: Relay) {
        switchContainer.write(channel, value.value)
    }

    override fun reset() {
        requestedValue = null
    }
}

class OneWireTemperatureInputPort(
    override val id: String,
    private val initialValue: Temperature)
    : InputPort<Temperature> {

    override var connectionValidUntil = Long.MAX_VALUE

    override val valueClazz = Temperature::class.java

    override fun read(): Temperature {
        return initialValue
    }
}
