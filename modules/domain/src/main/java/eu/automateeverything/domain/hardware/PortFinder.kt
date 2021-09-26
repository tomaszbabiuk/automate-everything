package eu.automateeverything.domain.hardware

interface PortFinder {
    fun <T : PortValue> listAllOfAnyType(valueClass: Class<T>) : List<Port<T>>
    fun <T : PortValue> listAllOfInputType(valueClass: Class<T>) : List<InputPort<T>>
    fun <T : PortValue> listAllOfOutputType(valueClass: Class<T>) : List<OutputPort<T>>
    fun <T : PortValue> searchForAnyPort(valueClazz: Class<T>, id:String): Port<*>
    fun <T : PortValue> searchForInputPort(valueClazz: Class<T>, id:String): InputPort<T>
    fun <T : PortValue> searchForOutputPort(valueClazz: Class<T>, id:String): OutputPort<T>
    fun checkNewPorts(): Boolean
    fun clearNewPortsFlag()
}