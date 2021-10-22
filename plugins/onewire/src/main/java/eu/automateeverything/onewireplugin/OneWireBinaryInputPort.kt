package eu.automateeverything.onewireplugin

import eu.automateeverything.domain.hardware.BinaryInput
import eu.automateeverything.domain.hardware.InputPort

class OneWireBinaryInputPort(
    override val id: String,
    private val initialValue: BinaryInput
)
    : InputPort<BinaryInput> {

    override var connectionValidUntil = Long.MAX_VALUE

    override val valueClazz = BinaryInput::class.java

    override fun read(): BinaryInput {
        return initialValue
    }
}