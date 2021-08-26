package eu.geekhome.domain.hardware

interface IPortFinder {
    fun <T : PortValue> searchForAnyPort(valueClazz: Class<T>, id:String): Port<*>
    fun <T : PortValue> searchForInputPort(valueClazz: Class<T>, id:String): InputPort<T>
    fun <T : PortValue> searchForOutputPort(valueClazz: Class<T>, id:String): OutputPort<T>
    fun checkNewPorts(): Boolean
    fun clearNewPortsFlag()
}