package eu.geekhome.services.hardware

interface IPortFinder {
    fun <T : PortValue> searchForPort(valueType: Class<T>, id:String, canRead: Boolean, canWrite: Boolean): Port<T>
    fun checkNewPorts()
}