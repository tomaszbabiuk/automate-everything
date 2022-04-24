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
import eu.automateeverything.domain.events.PortUpdateType
import eu.automateeverything.domain.hardware.*
import eu.automateeverything.domain.mqtt.MqttBrokerService
import eu.automateeverything.domain.mqtt.MqttListener
import eu.automateeverything.domain.langateway.LanGateway
import eu.automateeverything.domain.langateway.LanGatewayResolver
import eu.automateeverything.shellyplugin.ports.ShellyInputPort
import eu.automateeverything.shellyplugin.ports.ShellyOutputPort
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.*
import java.net.Inet4Address
import java.net.InetAddress
import java.util.*

class ShellyAdapter(
    owningPluginId: String,
    private val mqttBroker: MqttBrokerService,
    lanGatewayResolver: LanGatewayResolver,
    eventBus: EventBus
) : HardwareAdapterBase<ShellyInputPort<*>>(owningPluginId, "0", eventBus), MqttListener {
    private var brokerIP: Inet4Address? = null
    private var idBuilder = PortIdBuilder(owningPluginId)
    private val client = createHttpClient()
    private val lanGateways: List<LanGateway> = lanGatewayResolver.resolve()

    private fun createHttpClient() = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }

        engine {
            maxConnectionsCount = 1000

            endpoint {
                maxConnectionsPerRoute = 100
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 5000
                connectAttempts = 5
            }
        }
    }

    override suspend fun internalDiscovery(mode: DiscoveryMode) = coroutineScope {
        if (lanGateways.isEmpty()) {
            logDiscovery("The IP address of MQTT broker cannot be resolved - no LAN gateways! Aborting")
        } else {
            val defaultLanGateway = lanGateways.first()
            if (lanGateways.size > 1) {
                logDiscovery("WARNING! There's more than one LAN gateway. It's impossible to determine the correct IP address of MQTT broker (which should be same as Lan gateway). Using ${defaultLanGateway.interfaceName}")
            }
            brokerIP = defaultLanGateway.inet4Address
            val discoveryJob = async { ShellyHelper.searchForShellies(owningPluginId, client, brokerIP!!, eventBus) }
            val shellies = discoveryJob.await()

            shellies.forEach {
                val shellyIP = it.first
                val settingsResponse = it.second
                val shellyId = settingsResponse.device.hostname
                ShellyHelper.hijackShellyIfNeeded(client, brokerIP!!, settingsResponse, shellyIP)
                val statusResponse = ShellyHelper.callForStatus(client, shellyIP)

                val now = Calendar.getInstance()
                val portsFromDevice = ShellyPortFactory().constructPorts(shellyId, idBuilder, statusResponse, settingsResponse, now)
                addPotentialNewPorts(portsFromDevice)
            }
        }

        logDiscovery("Finished")
    }

    override fun executePendingChanges() {
        ports
            .values
            .filterIsInstance<ShellyOutputPort<*>>()
            .forEach {
                executeShellyChanges(it)
            }
    }

    private fun executeShellyChanges(shellyOutput: ShellyOutputPort<*>) {
        val mqttPayload = shellyOutput.getExecutePayload()
        if (mqttPayload != null) {
            val topic = shellyOutput.writeTopic
            mqttBroker.publish(topic, mqttPayload)
        }
    }

    override fun start() {
        mqttBroker.addMqttListener(this)
    }

    override fun stop() {
        mqttBroker.removeMqttListener(this)
    }

    override fun onPublish(clientID: String, topicName: String, msgAsString: String) {
        val now = Calendar.getInstance().timeInMillis

        ports
            .values
            .filter { it.readTopics.contains(topicName)  }
            .forEach {
                val prevValue = it.read().asDecimal()
                it.setValueFromMqttPayload(msgAsString)
                val newValue = it.read().asDecimal()
                if (prevValue != newValue) {
                    broadcastPortUpdate(PortUpdateType.ValueChange, it)
                }
            }

        ports
            .values
            .filter { it.id.contains(clientID) }
            .forEach {
                it.lastSeenTimestamp = now
                broadcastPortUpdate(PortUpdateType.LastSeenChange, it)
            }
    }

    override fun onDisconnected(clientID: String) {
        ports
            .values
            .forEach {
                val port = it
                if (port.id.contains(clientID)) {
                    port.lastSeenTimestamp = 0
                    broadcastPortUpdate(PortUpdateType.LastSeenChange, it)
                }
            }
    }

    override suspend fun onConnected(address: InetAddress) = withContext(Dispatchers.IO) {
        val finderResponse = ShellyHelper.checkIfShelly(owningPluginId, client, address, null)
        if (finderResponse != null) {
            val statusResponse = ShellyHelper.callForStatus(client, address)
            val settingsResponse = ShellyHelper.callForSettings(client, address)
            val shellyId = finderResponse.second.device.hostname
            val now = Calendar.getInstance()
            val portsFromDevice = ShellyPortFactory().constructPorts(
                shellyId,
                idBuilder,
                statusResponse,
                settingsResponse,
                now
            )

            addPotentialNewPorts(portsFromDevice)
        }
    }
}