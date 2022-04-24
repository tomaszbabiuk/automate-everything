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

package eu.automateeverything.aforeplugin

import eu.automateeverything.domain.events.EventBus
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import java.net.InetAddress

class AforeFinder(
    private val owningPluginId: String,
    private val client: HttpClient,
    private val machineIpAddress: InetAddress) {

    private suspend fun checkIfAfore(ipToCheck: InetAddress, eventBus: EventBus?) : Pair<InetAddress, String>? = coroutineScope {
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
            if (eventBus != null) {
                broadcastEvent(eventBus, "Afore found! Ip address: $ipToCheck")
            }

            Pair(ipToCheck, serialNumber)
        } else {
            null
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun searchForAforeDevices(eventBus: EventBus): List<Pair<InetAddress, String>> = coroutineScope {
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

        eventBus.broadcastDiscoveryEvent(
            owningPluginId,
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
                checkIfAfore(ipToCheck, eventBus)
            }

            jobs.add(job)
        }

        val result = jobs.awaitAll()
            .filterNotNull()
            .toList()

        broadcastEvent(eventBus,"Done looking for AFORE devices, found: ${result.size}")

        result
    }

    private fun broadcastEvent(eventBus: EventBus, message: String) {
        eventBus.broadcastDiscoveryEvent(owningPluginId, message)
    }
}