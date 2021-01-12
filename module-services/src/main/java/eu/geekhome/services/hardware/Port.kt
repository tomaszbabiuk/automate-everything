package eu.geekhome.services.hardware

open class Port<T,V: PortValue<T>>(
    val id: String,
    val value : PortValue<T>,
    val canRead: Boolean,
    val canWrite: Boolean
) {
    var isShadowed: Boolean = false; protected set
    var isOperational: Boolean = false; protected set
    var nonOperationalTime : Long = 0; protected set
}

class WattageInputPort(id: String, wattageValue: Double) :
    Port<Double, Wattage>(id, Wattage(wattageValue), true, false)
