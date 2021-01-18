package eu.geekhome.shellyplugin

import eu.geekhome.services.events.EventsSink
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import java.net.InetAddress

class ShellyFinder(private val client: HttpClient, private val brokerIP: InetAddress) {

    private suspend fun checkIfShelly(ipToCheck: InetAddress, eventsSink: EventsSink<String>) : Pair<InetAddress, ShellySettingsResponse>? = coroutineScope {
        try {
            val response = client.get<ShellySettingsResponse>("http://$ipToCheck/settings")
            eventsSink.broadcastEvent("One shelly found under ip address: $ipToCheck")
            Pair(ipToCheck,response)
        } catch (e: Exception) {
            eventsSink.broadcastEvent("$ipToCheck - unknown")
            null
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @ExperimentalCoroutinesApi
    suspend fun searchForShellies(eventsSink: EventsSink<String>): List<Pair<InetAddress, ShellySettingsResponse>> = coroutineScope {
        val jobs = ArrayList<Deferred<Pair<InetAddress, ShellySettingsResponse>?>>()

        for (i in 0..255) {
            val ipToCheck = InetAddress.getByAddress(
                byteArrayOf(
                    brokerIP.address[0],
                    brokerIP.address[1],
                    brokerIP.address[2],
                    i.toByte()
                )
            )

            val job = async(start = CoroutineStart.LAZY) {
                eventsSink.broadcastEvent("Is $ipToCheck a shelly device? Checking...")
                checkIfShelly(ipToCheck, eventsSink)
            }
            jobs.add(job)
        }

        val result = jobs.awaitAll()
            .filterNotNull()
            .filter { it.second.device != null && it.second.device.type != null}
            .toList()

        eventsSink.broadcastEvent("Done looking for shellies, found: ${result.size}")

        result
    }
}