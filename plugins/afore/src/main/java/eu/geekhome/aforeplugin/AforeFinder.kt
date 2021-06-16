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
    private val from: InetAddress,
    private val to: InetAddress) {

    private suspend fun checkIfAfore(ipToCheck: InetAddress, eventsSink: EventsSink?) : Pair<InetAddress, String>? = coroutineScope {
        var serialNumber: String? = null
        var hasPowerReadings = false

        try {
            val response = client.get<String>("http://$ipToCheck/status.html")
            val lines = response.split("\r\n").toTypedArray()
            for (line in lines) {
                if (line.startsWith("var webdata_sn = ")) {
                    serialNumber = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""))
                    println(serialNumber)
                } else if (line.startsWith("var webdata_now_p = ")) {
                    hasPowerReadings = true
                }
            }

            if (eventsSink != null) {
                broadcastEvent(eventsSink, "Afore found! Ip address: $ipToCheck")
            }
        } catch (ignored: Exception) {
        }

        if (serialNumber != null && hasPowerReadings) {
            Pair(ipToCheck, serialNumber)
        } else {
            null
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun searchForAforeDevices(eventsSink: EventsSink): List<Pair<InetAddress, String>> = coroutineScope {
        val jobs = ArrayList<Deferred<Pair<InetAddress, String>?>>()

        for (a0 in from.address[0]..to.address[0]) {
            for (a1 in from.address[1]..to.address[1]) {
                for (a2 in from.address[2]..to.address[2]) {
                    for (a3 in from.address[3]..to.address[3]) {
                        val ipToCheck = InetAddress.getByAddress(
                            byteArrayOf(a0.toByte(), a1.toByte(), a2.toByte(), a3.toByte())
                        )

                        broadcastEvent(eventsSink, "Checking address: $ipToCheck")
                        val job = async(start = CoroutineStart.LAZY) {
                            checkIfAfore(ipToCheck, eventsSink)
                        }

                        jobs.add(job)
                    }
                }
            }
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