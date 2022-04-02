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

package eu.automateeverything.zigbee2mqttplugin

import com.google.gson.Gson
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.hardware.*
import eu.automateeverything.domain.mqtt.MqttBrokerService
import eu.automateeverything.domain.mqtt.MqttListener
import eu.automateeverything.zigbee2mqttplugin.data.PermitJoinStatus
import kotlinx.coroutines.*
import java.net.InetAddress
import java.util.*

class Zigbee2MqttAdapter(
    private val owningPluginId: String,
    private val mqttBroker: MqttBrokerService,
    private val eventsSink: EventsSink
) : HardwareAdapterBase<Port<*>>(), MqttListener {
    private var permitJoin: Boolean = false
    override val id = ADAPTER_ID
    private var idBuilder = PortIdBuilder(owningPluginId)
    private val gson = Gson()

    override suspend fun internalDiscovery(eventsSink: EventsSink): ArrayList<Port<*>> = coroutineScope {
        val result = ArrayList<Port<*>>()

        permitToJoinProcedure()

        if (permitJoin) {
            logDiscovery("Pair new devices NOW! Counting 30 seconds down...")

            (0..6).forEach {
                delay(5000)
                println("${30-it*5}...")
            }

            mqttBroker.publish("zigbee2mqtt/bridge/request/permit_join", "false")
        } else {
            logDiscovery("It was impossible to permit other devices to join. Is Zigbee2Mqtt bridge running?")
        }

        result
    }

    private suspend fun permitToJoinProcedure() = coroutineScope {
        val permitJoinJob = async {
            (1..3).forEach {
                logDiscovery("Requesting new devices to join, trial $it")

                mqttBroker.publish("zigbee2mqtt/bridge/request/permit_join", "true")

                delay(5000)
            }
        }

        while (!permitJoin && permitJoinJob.isActive) {
            //waiting for Zigbee2Mqtt bridge for allowing other devices to join
        }

        if (permitJoinJob.isActive) {
            permitJoinJob.cancel()
        }
    }

    private fun logDiscovery(message: String) {
        eventsSink.broadcastDiscoveryEvent(
            owningPluginId,
            message
        )
    }
    override fun executePendingChanges() {

    }

    override fun start() {
        mqttBroker.addMqttListener(this)
    }

    override fun stop() {
        mqttBroker.removeMqttListener(this)
    }

    override fun onPublish(clientID: String, topicName: String, msgAsString: String) {
        if (topicName.startsWith("zigbee2mqtt")) {
            if (topicName == "zigbee2mqtt/bridge/response/permit_join") {
                handlePermitJoinMessage(msgAsString)
            }

            println(clientID)
            println(topicName)
            println(msgAsString)
        }
    }

    private fun handlePermitJoinMessage(msgAsString: String) {
        val status = gson.fromJson(msgAsString, PermitJoinStatus::class.java)
        permitJoin = status.data.value
    }

    override fun onDisconnected(clientID: String) {

    }

    override suspend fun onConnected(address: InetAddress) = withContext(Dispatchers.IO) {

    }

    companion object {
        const val ADAPTER_ID = "0"
    }
}

/*

zigbee2mqtt/bridge/response/permit_join
{"data":{"value":true},"status":"ok"}
 */