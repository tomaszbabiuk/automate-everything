package eu.geekhome.langateway

import eu.geekhome.domain.langateway.LanGateway
import eu.geekhome.domain.langateway.LanGatewayResolver
import java.net.Inet4Address
import java.net.NetworkInterface

class JavaLanGatewayResolver : LanGatewayResolver {
    override fun resolve(): List<LanGateway> {
        return NetworkInterface
            .getNetworkInterfaces()
            .toList()
            .filterNot { it.displayName.contains("VMware") }
            .filterNot { it.displayName.contains("VirtualBox") }
            .filter { networkInterface ->
                networkInterface.inetAddresses.toList()
                    .filterIsInstance<Inet4Address>()
                    .any { address -> address.isSiteLocalAddress }
            }
            .map {
                val interfaceName = it.displayName
                val address = it.inetAddresses.toList().filterIsInstance<Inet4Address>().first()
                LanGateway(interfaceName, address)
            }
    }
}