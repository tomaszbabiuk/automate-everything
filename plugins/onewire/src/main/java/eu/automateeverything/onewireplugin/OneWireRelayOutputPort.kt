package eu.automateeverything.onewireplugin

import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.Relay

class OneWireRelayOutputPort(
    override val id: String,
    private var switchContainer: BinaryInputsCoordinator,
    private val channel: Int            )
    : OutputPort<Relay> {

    override var connectionValidUntil = Long.MAX_VALUE

    override val valueClazz = Relay::class.java
    override var requestedValue : Relay? = null

    override fun read(): Relay {
        return Relay(switchContainer.cachedRead(channel))
    }

    override fun write(value: Relay) {
        switchContainer.write(channel, value.value)
    }

    override fun reset() {
        requestedValue = null
    }
}