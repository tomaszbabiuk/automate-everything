package eu.geekhome.shellyplugin

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

object LanInetAddressResolver {

    fun resolve(): InetAddress? {
        val addresses: MutableList<InetAddress> = ArrayList()
        val interfaces = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val cur = interfaces.nextElement()
            if (cur.isLoopback) {
                continue
            }
            for (interfaceAddress in cur.interfaceAddresses) {
                if (interfaceAddress.broadcast == InetAddress.getByAddress(byteArrayOf(192.toByte(),168.toByte(),1.toByte(),255.toByte()))) {
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