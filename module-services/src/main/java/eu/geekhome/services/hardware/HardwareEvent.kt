package eu.geekhome.services.hardware

interface IHardwareEvent {
    val factoryId: String
}

data class HardwareEvent(override val factoryId: String,
                         val message: String) : IHardwareEvent

data class PortUpdateEvent(override val factoryId: String,
                           val adapterId: String,
                           val port: Port<*>) : IHardwareEvent
