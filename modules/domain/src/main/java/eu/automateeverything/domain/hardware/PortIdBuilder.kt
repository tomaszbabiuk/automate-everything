package eu.automateeverything.domain.hardware

class PortIdBuilder(private val factoryId: String, private val adapterId: String) {
    fun buildPortId(portPrefix: String, channel: Int, portSuffix: String): String {
        return "$factoryId-$adapterId:$portPrefix:$portSuffix-$channel"
    }
}