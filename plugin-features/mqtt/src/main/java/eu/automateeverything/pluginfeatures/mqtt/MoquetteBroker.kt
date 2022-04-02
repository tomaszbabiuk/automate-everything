/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
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

package eu.automateeverything.pluginfeatures.mqtt

import eu.automateeverything.domain.mqtt.MqttBrokerService
import eu.automateeverything.domain.mqtt.MqttListener
import io.moquette.interception.AbstractInterceptHandler
import io.moquette.interception.messages.InterceptPublishMessage
import io.moquette.interception.messages.InterceptConnectionLostMessage
import io.moquette.interception.messages.InterceptConnectMessage
import java.net.InetAddress
import kotlin.Throws
import java.io.IOException
import io.moquette.interception.InterceptHandler
import io.moquette.broker.config.MemoryConfig
import io.moquette.BrokerConstants
import io.moquette.broker.Server
import io.netty.handler.codec.mqtt.MqttMessageBuilders
import io.netty.handler.codec.mqtt.MqttQoS
import io.netty.buffer.Unpooled
import kotlinx.coroutines.*
import java.net.UnknownHostException
import java.util.*

class MoquetteBroker : MqttBrokerService {

    private val broker: Server = Server()
    private val listeners: MutableList<MqttListener> = ArrayList()
    var serviceScope: CoroutineScope? = null

    override fun addMqttListener(listener: MqttListener) {
        listeners.add(listener)
    }

    override fun removeMqttListener(listener: MqttListener) {
        listeners.remove(listener)
    }

    internal class PublisherListener(
        private val brokerActiveScope: CoroutineScope,
        private val listeners: List<MqttListener>,
        private val server: Server
    ) :
        AbstractInterceptHandler() {
        override fun getID(): String {
            return "InternalPublishListener"
        }

        override fun onPublish(msg: InterceptPublishMessage) {
            var size = 0
            val buffer = ByteArray(102400)
            while (msg.payload.isReadable) {
                buffer[size] = msg.payload.readByte()
                size++
            }
            val msgAsString = String(buffer, 0, size)
            for (listener in listeners) {
                listener.onPublish(msg.clientID, msg.topicName, msgAsString)
            }
        }

        override fun onConnectionLost(msg: InterceptConnectionLostMessage) {
            val clientID = msg.clientID
            for (listener in listeners) {
                listener.onDisconnected(clientID)
            }
        }

        @Suppress("BlockingMethodInNonBlockingContext")
        override fun onConnect(msg: InterceptConnectMessage) {
            super.onConnect(msg)
            brokerActiveScope.launch {
                if (isActive) for (client in server.listConnectedClients()) {
                    if (msg.clientID == client.clientID) {
                        for (listener in listeners) {
                            try {
                                val address = InetAddress.getByName(client.address)
                                listener.onConnected(address)
                            } catch (ignored: UnknownHostException) {
                            }
                        }
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    override fun start() {
        if (serviceScope != null) {
            serviceScope?.cancel("MQTT Broker already started")
        }
        serviceScope = CoroutineScope(Dispatchers.IO)
        val userHandlers: List<InterceptHandler?> = listOf(PublisherListener(serviceScope!!, listeners, broker))
        val memoryConfig = MemoryConfig(Properties())
        memoryConfig.setProperty(BrokerConstants.ALLOW_ANONYMOUS_PROPERTY_NAME, "true")
        memoryConfig.setProperty(BrokerConstants.NETTY_MAX_BYTES_PROPERTY_NAME, "102400")
        broker.startServer(memoryConfig, userHandlers)
    }

    override fun stop() {
        serviceScope?.cancel("MQTT Broker has been stopped")
        serviceScope = null
        broker.stopServer()
    }

    override fun publish(topic: String?, content: String) {
        val message = MqttMessageBuilders.publish()
            .topicName(topic)
            .retained(true)
            .qos(MqttQoS.EXACTLY_ONCE)
            .payload(Unpooled.copiedBuffer(content.toByteArray()))
            .build()
        broker.internalPublish(message, "geekHOME Server 2")
    }
}