package eu.geekhome.services.hardware

interface IPortFinder {
    fun <T : PortValue<*>> searchForPort(id:String, canRead: Boolean, canWrite: Boolean): Port<T>
}