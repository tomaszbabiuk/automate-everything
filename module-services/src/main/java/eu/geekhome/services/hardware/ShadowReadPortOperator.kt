package eu.geekhome.services.hardware

class ShadowReadPortOperator<V>(private val staticValue: V) : ReadPortOperator<V> {
    override fun read(): V {
        return staticValue
    }
}

class ShadowWritePortOperator<V>(private val initialValue: V) : WritePortOperator<V> {

    private var value: V = initialValue

    override fun write(value: V) {
        this.value = value
    }

    override fun reset() {
    }

}