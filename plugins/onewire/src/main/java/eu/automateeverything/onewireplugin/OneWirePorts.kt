package eu.automateeverything.onewireplugin

import eu.automateeverything.domain.hardware.BinaryInput
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