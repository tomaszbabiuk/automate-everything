package eu.geekhome.shellyplugin

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

object LanInetAddressHelper {

    @get:Throws(SocketException::class)
    val ipsInLan: List<InetAddress>
        get() {
            val addresses: MutableList<InetAddress> = ArrayList()
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val cur = interfaces.nextElement()
                if (cur.isLoopback) {
                    continue
                }
                for (addr in cur.interfaceAddresses) {
                    val inet_addr = addr.address
                    if (inet_addr is Inet4Address) {
                        addresses.add(inet_addr)
                    }
                }
            }
            return addresses
        }
}