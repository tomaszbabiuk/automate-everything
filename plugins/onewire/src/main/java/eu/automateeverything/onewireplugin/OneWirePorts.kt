package eu.automateeverything.onewireplugin

import eu.automateeverything.domain.hardware.BinaryInput
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.Relay
import eu.automateeverything.domain.hardware.Temperature

class OneWireTemperatureInputPort(
    override val id: String,
    override val address: ByteArray,
    override var value: Temperature,
) : OneWirePort<Temperature>() {

    override val valueClazz = Temperature::class.java
}

class OneWireBinaryInputPort(
    override val id: String,
    val channel: Int,
    override val address: ByteArray,
    override var value: BinaryInput,
) : OneWirePort<BinaryInput>() {

    override val valueClazz = BinaryInput::class.java
}

class OneWireRelayPort(
    override val id: String,
    val channel: Int,
    override val address: ByteArray,
    override var value: Relay,
) : OneWirePort<Relay>(), OutputPort<Relay> {

    override val valueClazz = Relay::class.java

    override fun write(value: Relay) {
        requestedValue = value
    }

    fun commit() {
        if (requestedValue != null) {
            value.value = requestedValue!!.value
            reset()
        }
    }

    override var requestedValue: Relay? = null

    override fun reset() {
        requestedValue = null
    }
}