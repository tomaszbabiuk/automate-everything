package eu.automateeverything.domain.langateway

import java.net.Inet4Address

data class LanGateway(
    val interfaceName: String,
    val inet4Address: Inet4Address
)