/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.shellyplugin

import eu.automateeverything.domain.events.EventBus
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import java.io.IOException
import java.net.Inet4Address
import java.net.InetAddress

object ShellyHelper {

    suspend fun checkIfShelly(owningPluginId: String, client: HttpClient, ipToCheck: InetAddress, eventBus: EventBus?) : Pair<InetAddress, ShellySettingsResponse>? = coroutineScope {
        try {
            val response = client.get<ShellySettingsResponse>("http://$ipToCheck/settings")
            eventBus?.broadcastDiscoveryEvent(
                owningPluginId,
                "Shelly found! Ip address: $ipToCheck"
            )
            Pair(ipToCheck,response)
        } catch (e: Exception) {
            null
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun searchForShellies(owningPluginId: String, client: HttpClient, brokerIP: InetAddress, eventBus: EventBus): List<Pair<InetAddress, ShellySettingsResponse>> = withContext(Dispatchers.IO) {
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

        eventBus.broadcastDiscoveryEvent(
            owningPluginId,
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
                checkIfShelly(owningPluginId, client, ipToCheck, eventBus)
            }
            jobs.add(job)
        }

        val result = jobs.awaitAll()
            .filterNotNull()
            .toList()

        eventBus.broadcastDiscoveryEvent(
            owningPluginId,
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