package eu.geekhome.shellyplugin

import eu.geekhome.services.events.EventsSink
import eu.geekhome.services.hardware.HardwareEvent
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import java.net.InetAddress

class ShellyFinder(private val client: HttpClient, private val brokerIP: InetAddress) {

    private suspend fun checkIfShelly(ipToCheck: InetAddress, eventsSink: EventsSink<HardwareEvent>) : Pair<InetAddress, ShellySettingsResponse>? = coroutineScope {
        try {
            val response = client.get<ShellySettingsResponse>("http://$ipToCheck/settings")
            broadcastEvent(eventsSink,"One shelly found under ip address: $ipToCheck")
            Pair(ipToCheck,response)
        } catch (e: Exception) {
            broadcastEvent(eventsSink,"$ipToCheck - unknown")
            null
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @ExperimentalCoroutinesApi
    suspend fun searchForShellies(eventsSink: EventsSink<HardwareEvent>): List<Pair<InetAddress, ShellySettingsResponse>> = coroutineScope {
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
                broadcastEvent(eventsSink,"Is $ipToCheck a shelly device? Checking...")
                checkIfShelly(ipToCheck, eventsSink)
            }
            jobs.add(job)
        }

        val result = jobs.awaitAll()
            .filterNotNull()
            .toList()

        broadcastEvent(eventsSink,"Done looking for shellies, found: ${result.size}")

        result
    }

    fun broadcastEvent(eventsSink: EventsSink<HardwareEvent>, message: String) {
        val event = HardwareEvent(ShellyAdapterFactory.ID, message)
        eventsSink.broadcastEvent(event)
    }
}