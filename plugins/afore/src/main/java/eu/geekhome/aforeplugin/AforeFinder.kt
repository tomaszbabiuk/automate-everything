package eu.geekhome.aforeplugin

import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.events.DiscoveryEventData
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import java.net.InetAddress

class AforeFinder(
    private val owningPluginId: String,
    private val client: HttpClient,
    private val machineIpAddress: InetAddress) {

    private suspend fun checkIfAfore(ipToCheck: InetAddress, eventsSink: EventsSink?) : Pair<InetAddress, String>? = coroutineScope {
        var serialNumber: String? = null
        var hasPowerReadings = false

        try {
            val response = client.get<String>("http://$ipToCheck/status.html")
            val lines = response.split("\r\n").toTypedArray()
            for (line in lines) {
                if (line.startsWith("var webdata_sn = ")) {
                    serialNumber = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""))
                } else if (line.startsWith("var webdata_now_p = ")) {
                    hasPowerReadings = true
                }
            }
        } catch (ignored: Exception) {
        }

        if (serialNumber != null && hasPowerReadings) {
            if (eventsSink != null) {
                broadcastEvent(eventsSink, "Afore found! Ip address: $ipToCheck")
            }

            Pair(ipToCheck, serialNumber)
        } else {
            null
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun searchForAforeDevices(eventsSink: EventsSink): List<Pair<InetAddress, String>> = coroutineScope {
        val jobs = ArrayList<Deferred<Pair<InetAddress, String>?>>()

        val lookupAddressBegin =  InetAddress.getByAddress(
            byteArrayOf(
                machineIpAddress.address[0],
                machineIpAddress.address[1],
                machineIpAddress.address[2],
                0)
        )

        val lookupAddressEnd =  InetAddress.getByAddress(
            byteArrayOf(
                machineIpAddress.address[0],
                machineIpAddress.address[1],
                machineIpAddress.address[2],
                255.toByte())
        )

        eventsSink.broadcastDiscoveryEvent(
            AforePlugin.PLUGIN_ID_AFORE,
            "Looking for afore devices in LAN, the IP address range is $lookupAddressBegin - $lookupAddressEnd"
        )

        for (i in 0..255) {
            val ipToCheck = InetAddress.getByAddress(
                byteArrayOf(
                    machineIpAddress.address[0],
                    machineIpAddress.address[1],
                    machineIpAddress.address[2],
                    i.toByte())
            )

            val job = async(start = CoroutineStart.LAZY) {
                checkIfAfore(ipToCheck, eventsSink)
            }

            jobs.add(job)
        }

        val result = jobs.awaitAll()
            .filterNotNull()
            .toList()

        broadcastEvent(eventsSink,"Done looking for AFORE devices, found: ${result.size}")

        result
    }

    private fun broadcastEvent(eventsSink: EventsSink, message: String) {
        val event = DiscoveryEventData(owningPluginId, message)
        eventsSink.broadcastEvent(event)
    }
}