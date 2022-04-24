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
import eu.automateeverything.domain.events.PortUpdateType
import eu.automateeverything.domain.hardware.DiscoveryMode
import eu.automateeverything.domain.hardware.HardwareAdapterBase
import eu.automateeverything.domain.hardware.PortIdBuilder
import eu.automateeverything.domain.langateway.LanGateway
import eu.automateeverything.domain.langateway.LanGatewayResolver
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.*
import java.util.*

class AforeAdapter(
    owningPluginId: String,
    private val lanGatewayResolver: LanGatewayResolver,
    eventBus: EventBus) : HardwareAdapterBase<AforeWattageInputPort>(owningPluginId, "0", eventBus) {
    var operationScope: CoroutineScope? = null
    private val httpClient = createHttpClient()
    private val idBuilder = PortIdBuilder(owningPluginId)

    private fun createHttpClient() = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }

        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(username = "admin", password = "admin")
                }
            }
        }

        engine {
            maxConnectionsCount = 1000

            endpoint {
                maxConnectionsPerRoute = 100
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 10000
                connectAttempts = 5
            }
        }
    }

    override suspend fun internalDiscovery(mode: DiscoveryMode) = coroutineScope {
        val result = ArrayList<AforeWattageInputPort>()
        logDiscovery("Starting AFORE discovery")

        val lanGateways: List<LanGateway> = lanGatewayResolver.resolve()
        val lanGateway: LanGateway? = when (lanGateways.size) {
            0 -> {
                logDiscovery("Cannot resolve LAN gateway... aborting!")
                null
            }
            1 -> {
                lanGateways.first()
            }
            else -> {
                logDiscovery("There's more than one LAN gateways. Using the first one: ${lanGateways.first().interfaceName}!")
                lanGateways.first()
            }
        }

        if (lanGateway != null) {
            val machineIPAddress = lanGateway.inet4Address
            val finder = AforeFinder(owningPluginId, httpClient, machineIPAddress)
            val discoveryJob = async { finder.searchForAforeDevices(eventBus) }
            val aforeDevices = discoveryJob.await()

            aforeDevices.forEach {
                eventBus.broadcastDiscoveryEvent(owningPluginId,"AFORE inverter found, IP:${it.first}, s/n:${it.second}")
                val portId = idBuilder.buildPortId(it.second, 0.toString(), "W")
                val inverterPort = AforeWattageInputPort(portId, httpClient, it.first)
                result.add(inverterPort)
            }
        }

        eventBus.broadcastDiscoveryEvent(owningPluginId, "Finished")

        addPotentialNewPorts(result)
    }

    private suspend fun maintenanceLoop(now: Calendar) {
        ports.values.forEach {
            it.refresh(now)
            it.lastSeenTimestamp = now.timeInMillis
            broadcastPortUpdate(PortUpdateType.LastSeenChange, it)

            delay(30000)
        }
    }

    override fun executePendingChanges() {
        //This adapter is read-only
    }

    override fun stop() {
        operationScope?.cancel("Stop called")
    }

    override fun start() {
        if (operationScope != null) {
            operationScope!!.cancel("Adapter already started")
        }

        operationScope = CoroutineScope(Dispatchers.IO)
        operationScope?.launch(CoroutineName("afore_maintenance_loop")) {
            while (isActive) {
                maintenanceLoop(Calendar.getInstance())
            }
        }
    }
}

