/*
 * Copyright (c) 2022 Tomasz Babiuk
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

package eu.automateeverything.mobileaccessplugin

import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.mqtt3.Mqtt3BlockingClient
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import eu.automateeverything.domain.WithStartStopScope
import eu.automateeverything.domain.inbox.Inbox
import eu.automateeverything.interop.ByteArraySessionHandler
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import saltchannel.util.Hex
import saltchannel.util.KeyPair
import saltchannel.util.Rand
import saltchannel.v2.SaltServerSession
import java.nio.charset.StandardCharsets.UTF_8
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean


class MqttSaltServer(
    private val brokerAddress: BrokerAddress,
    private val secretsPassword: String,
    private val inbox: Inbox,
    private val sessionHandler: ByteArraySessionHandler,
    private val channelActivator: ChannelActivator
)
    : WithStartStopScope<List<String>>() {

    enum class ConnectionState {
        Initialization, Disconnected, Connecting, Connected
    }

    private val logger = LoggerFactory.getLogger(MqttSaltServer::class.java)
    private var contexts: List<ConnectorContext>? = null
    private val random = Rand { b -> SecureRandom.getInstanceStrong().nextBytes(b) }

    private var lastConnectionState = true
    private var lastStartParams: List<String>? = null
    private var connectionState = ConnectionState.Initialization

    private val client =  buildClient()

    private fun buildClient(): Mqtt3BlockingClient {
        val builder = MqttClient.builder()
            .useMqttVersion3()
            .serverHost(brokerAddress.host)
            .serverPort(brokerAddress.port)
            .addDisconnectedListener { connectionState = ConnectionState.Disconnected }

         if (brokerAddress.tlsRequired) {
             builder.sslWithDefaultConfig()
         }

         return builder.buildBlocking()
    }


    private fun connect() {
        connectionState = ConnectionState.Connecting
        try {
            logger.info("Connecting to: $brokerAddress")

            if (brokerAddress.user.isNotEmpty() && brokerAddress.password.isNotEmpty()) {
                client.connectWith()
                    .simpleAuth()
                    .username(brokerAddress.user)
                    .password(UTF_8.encode(brokerAddress.password))
                    .applySimpleAuth()
                    .send()
            } else {
                client.connectWith().send()
            }

            connectionState = ConnectionState.Connected
        } catch (ex: Exception) {
            logger.info("A problem connecting to mqtt broker: ${brokerAddress.host}", ex)
        }
    }

    override fun start(params: List<String>) {
        super.start(params)
        lastStartParams = params

        startStopScope.launch {
            try {
                if (connectionState == ConnectionState.Disconnected || connectionState == ConnectionState.Initialization) {
                    connect()
                }

                if (connectionState == ConnectionState.Connected && params.isNotEmpty()) {
                    rebuildContexts(params)
                }
            } catch (ex: Exception) {
                logger.error("A problem connecting to MQTT broker", ex)
            }

            mqttBrokerSupervisorLoop()
        }
    }

    private suspend fun mqttBrokerSupervisorLoop() = coroutineScope {
        while (isActive) {
            delay(1000 * 30L)

            try {
                if (connectionState == ConnectionState.Disconnected) {
                    connect()
                }

                if (connectionState == ConnectionState.Disconnected && lastConnectionState) {
                    inbox.sendMessage(
                        R.inbox_message_mqtt_server_error_subject,
                        R.inbox_message_broker_disconnected
                    )

                    cancelContexts()
                }

                if (connectionState == ConnectionState.Connected && !lastConnectionState) {
                    inbox.sendMessage(
                        R.inbox_message_mqtt_server_error_subject,
                        R.inbox_message_broker_connected
                    )

                    if (lastStartParams != null) {
                        rebuildContexts(lastStartParams!!)
                    }
                }
            } catch (ex: Exception) {
                logger.error("Unhandled exception during supervising MQTT connection", ex)
            }

            lastConnectionState = connectionState == ConnectionState.Connected
        }
    }

    private fun buildContext(params: List<String>, scope: CoroutineScope): List<ConnectorContext> {
        val storage = SecretStorage()
        return params.mapNotNull { serverSignPubKeyHex ->
            val keyPair = storage.loadSecret(secretsPassword, serverSignPubKeyHex)
            if (keyPair != null) {
                ConnectorContext(keyPair, scope)
            } else {
                inbox.sendMessage(
                    R.inbox_message_mqtt_server_error_subject,
                    R.inbox_message_mqtt_server_error_missing_keypair
                )
                null
            }
        }
    }

    override fun stop() {
        try {
            cancelContexts()
            if (connectionState == ConnectionState.Connected || connectionState == ConnectionState.Connecting) {
                client.disconnect()
            }
        } catch (ex: Exception) {
            logger.error("A problem while stopping MQTT Salt Server", ex)
        }
        super.stop()
    }

    private fun cancelContexts() {
        contexts?.forEach {
            it.cancel()
        }
    }

    private fun rebuildContexts(params: List<String>) {
        contexts = buildContext(params, startStopScope)
    }

    inner class SingleSessionContext(
        signKeyPair: KeyPair,
        scope: CoroutineScope,
        publisher: (ByteArray) -> Unit
    ) {
        fun push(payload: ByteArray) {
            channel.offer(payload)
        }

        override fun toString(): String {
            return "${hashCode()}, job.active=${job.isActive}, job.cancelled=${job.isCancelled}, job.completed=${job.isCompleted}"
        }

        fun cancel() {
            sessionCancellationToken.set(true)
            job.cancel("Forced cancellation")
        }

        fun isDone(): Boolean {
            return job.isCompleted || job.isCancelled
        }

        private var session: SaltServerSession
        private var channel: QueuedCancellableByteChannel
        private val sessionCancellationToken = AtomicBoolean(false)
        private var job: Deferred<Unit>

        init {
            channel = QueuedCancellableByteChannel(sessionCancellationToken, publisher)
            session = SaltServerSession(signKeyPair, channel)
            session.setEncKeyPair(random)
            job = scope.async {
                try {
                    session.handshake()

                    channelActivator.activateChannel(signKeyPair.pub(), session.clientSigKey)

                    while (isActive && !sessionCancellationToken.get()) {
                        val incomingData = session.channel.read("incoming packets")
                        logger.debug("Incoming session data ${incomingData.toHexString()}")
                        val isLast = session.channel.lastFlag()
                        val responses = sessionHandler.handleRequest(incomingData)
                        session.channel.write(false, responses)

                        if (isLast) {
                            break
                        }
                    }

                    logger.debug("Unhandled exception from MQTT server")
                } catch (ex: ChannelTerminatedException) {
                    logger.debug("Unhandled exception from MQTT server", ex)
                } catch (ex: Exception) {
                    logger.error("Unhandled exception from MQTT server", ex)
                }
            }
        }
    }

    inner class ConnectorContext(val keyPair: KeyPair, private val scope: CoroutineScope) {
        private val sessions = LinkedHashMap<String, SingleSessionContext>()

        init {
            val serverSignPubKeyHex = String(Hex.toHexCharArray(keyPair.pub(), 0, keyPair.pub().size))

            val inboundTopic = "$serverSignPubKeyHex/+/rx"
            logger.debug("Subscribing to $inboundTopic")

            client.subscribeWith()
                .topicFilter(inboundTopic)
                .send()

            client.toAsync().publishes(MqttGlobalPublishFilter.ALL) { publish: Mqtt3Publish ->
                val discriminator = publish.topic.levels[1]

                if (sessions.containsKey(discriminator)) {
                    val context = sessions[discriminator]!!
                    context.push(publish.payloadAsBytes)
                } else {
                    val newContext = SingleSessionContext(keyPair, scope) { payload ->
                        val txTopic = "$serverSignPubKeyHex/$discriminator/tx"
                        client.publishWith()
                            .topic(txTopic)
                            .payload(payload)
                            .send()
                    }
                    cleanupSessions()
                    sessions[discriminator] = newContext
                    newContext.push(publish.payloadAsBytes)
                }
            }
        }

        private fun cleanupSessions() {
            fun closeAndRemoveSession(key: String) {
                val sessionToRemove = sessions[key]!!
                sessionToRemove.cancel()
                sessions.remove(key)
            }

            val keysToRemove = sessions.filterValues { it.isDone() }.map {it.key }
            keysToRemove.forEach {
                closeAndRemoveSession(it)
            }

            while (sessions.keys.size > MAX_OPEN_SESSIONS_PER_CONNECTOR) {
                val firstKeyToRemove = sessions.keys.first()
                closeAndRemoveSession(firstKeyToRemove)
            }
        }

        fun cancel() {
            sessions.values.forEach { it.cancel() }
        }
    }

    companion object {
        const val MAX_OPEN_SESSIONS_PER_CONNECTOR = 3
    }
}

fun ByteArray.toHexString() = joinToString(" ") { "%02X".format(it) }