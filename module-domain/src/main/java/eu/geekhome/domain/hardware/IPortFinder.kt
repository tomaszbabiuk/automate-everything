package eu.geekhome.domain.hardware

interface IPortFinder {
    fun <T : PortValue> searchForAnyPort(valueType: Class<T>, id:String): Port<*>
    fun <T : PortValue> searchForInputPort(valueType: Class<T>, id:String): InputPort<T>
    fun <T : PortValue> searchForOutputPort(valueType: Class<T>, id:String): OutputPort<T>
    fun checkNewPorts(): Boolean
}