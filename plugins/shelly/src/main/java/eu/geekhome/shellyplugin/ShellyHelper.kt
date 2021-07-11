package eu.geekhome.shellyplugin

import eu.geekhome.domain.events.EventsSink
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import java.io.IOException
import java.net.Inet4Address
import java.net.InetAddress

object ShellyHelper {

    suspend fun checkIfShelly(client: HttpClient, ipToCheck: InetAddress, eventsSink: EventsSink?) : Pair<InetAddress, ShellySettingsResponse>? = coroutineScope {
        try {
            val response = client.get<ShellySettingsResponse>("http://$ipToCheck/settings")
            eventsSink?.broadcastDiscoveryEvent(
                ShellyPlugin.PLUGIN_ID_SHELLY,
                "Shelly found! Ip address: $ipToCheck"
            )
            Pair(ipToCheck,response)
        } catch (e: Exception) {
            null
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun searchForShellies(client: HttpClient, brokerIP: InetAddress, eventsSink: EventsSink): List<Pair<InetAddress, ShellySettingsResponse>> = withContext(Dispatchers.IO) {
        val lookupAddressBegin = InetAddress.getByAddress(
            byteArrayOf(
                brokerIP.address[0], brokerIP.address[1], brokerIP.address[2],
                0.toByte()
            )
        )
        val lookupAddressEnd = InetAddress.getByAddress(
            byteArrayOf(
                brokerIP.address[0], brokerIP.address[1], brokerIP.address[2],
                255.toByte()
            )
        )

        eventsSink.broadcastDiscoveryEvent(
            ShellyPlugin.PLUGIN_ID_SHELLY,
            "Looking for shelly devices in LAN, the IP address range is $lookupAddressBegin - $lookupAddressEnd"
        )

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
                checkIfShelly(client, ipToCheck, eventsSink)
            }
            jobs.add(job)
        }

        val result = jobs.awaitAll()
            .filterNotNull()
            .toList()

        eventsSink.broadcastDiscoveryEvent(
            ShellyPlugin.PLUGIN_ID_SHELLY,
            "Done looking for shellies, found: ${result.size}"
        )

        result
    }

    @Throws(IOException::class)
    suspend fun hijackShellyIfNeeded(client: HttpClient, brokerIP: Inet4Address, settings: ShellySettingsResponse, shellyIP: InetAddress) {
        if (settings.cloud.enabled) {
            disableCloud(client, shellyIP)
        }
        val expectedMqttServerSetting = brokerIP.hostAddress + ":1883"
        if (!settings.mqtt.enable || settings.mqtt.server != expectedMqttServerSetting) {
            enableMQTT(client, brokerIP, shellyIP)
        }
    }

    private suspend fun enableMQTT(client: HttpClient, brokerIP: InetAddress, shellyIP: InetAddress) {
        client.get<String>("""http://$shellyIP/settings/mqtt?mqtt_enable=1&mqtt_server=${brokerIP.hostAddress}%3A1883""")
    }

    suspend fun callForStatus(client: HttpClient, shellyIP: InetAddress): ShellyStatusResponse {
        return client.get("http://$shellyIP/status")
    }

    suspend fun callForSettings(client: HttpClient, shellyIP: InetAddress): ShellySettingsResponse {
        return client.get("http://$shellyIP/settings")
    }

    private suspend fun disableCloud(client: HttpClient, shellyIP: InetAddress) {
        client.get<String>("http://$shellyIP/settings/cloud?enabled=0")
    }
}