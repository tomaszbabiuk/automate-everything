package eu.automateeverything.domain.hardware

class PortIdBuilder(private val factoryId: String) {
    fun buildPortId(portPrefix: String, channel: String, portSuffix: String): String {
        return "$factoryId:$portPrefix:$portSuffix-$channel"
    }
}