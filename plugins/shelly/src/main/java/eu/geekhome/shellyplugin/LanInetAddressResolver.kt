package eu.geekhome.shellyplugin

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface

object LanInetAddressResolver {

    fun resolve(): InetAddress? {
        val interfaces = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val cur = interfaces.nextElement()
            if (cur.isLoopback) {
                continue
            }
            for (interfaceAddress in cur.interfaceAddresses) {
                if (interfaceAddress.broadcast.isSiteLocalAddress) {
                    val address = interfaceAddress.address
                    if (address is Inet4Address) {
                        return address
                    }
                }
            }
        }

        return null
    }
}