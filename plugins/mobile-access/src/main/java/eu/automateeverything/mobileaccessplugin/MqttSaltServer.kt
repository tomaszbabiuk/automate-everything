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

import eu.automateeverything.domain.WithStartStopScope
import eu.automateeverything.domain.inbox.Inbox
import eu.automateeverything.interop.ByteArraySessionHandler
import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import saltchannel.util.Hex
import saltchannel.util.KeyPair
import saltchannel.util.Rand
import saltchannel.v2.SaltServerSession
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class MqttSaltServer(
    private val brokerAddress: String,
    private val secretsPassword: String,
    private val inbox: Inbox,
    private val sessionHandler: ByteArraySessionHandler,
    private val channelActivator: ChannelActivator
)
    : WithStartStopScope<List<String>>() {

    private var contexts: List<ConnectorContext>? = null
    private val random = Rand { b -> SecureRandom.getInstanceStrong().nextBytes(b) }

    private val client = MqttClient(brokerAddress, "Automate Everything - Mobile Access", MemoryPersistence())

    private fun connect() {
        val options = MqttConnectOptions()
        options.isAutomaticReconnect = true
        options.isCleanSession = true
        options.connectionTimeout = 10
        println("Connecting to: $brokerAddress")
        client.connect(options)
        println("Connected")
    }

    override fun start(params: List<String>) {
        super.start(params)

        if (!client.isConnected) {
            connect()
        }

        if (params.isNotEmpty()) {
            contexts = buildContext(params, startStopScope)
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
        contexts?.forEach {
            it.cancel()
        }

        client.disconnect()
        super.stop()
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
                        println("Incoming session data ${incomingData.toHexString()}")
                        val isLast = session.channel.lastFlag()
                        val responses = sessionHandler.handleRequest(incomingData)
                        session.channel.write(false, responses)

                        if (isLast) {
                            break
                        }
                    }

                    println("context finishes")
                } catch (ex: ChannelTerminatedException) {
                    println("channel terminated")
                } catch (ex: Exception) {
                    //TODO: handle case when message is too big

                    println("context has failed, $ex")
                }
            }
        }
    }

    inner class ConnectorContext(val keyPair: KeyPair, private val scope: CoroutineScope) {
        private val sessions = LinkedHashMap<String, SingleSessionContext>()

        init {
            val serverSignPubKeyHex = String(Hex.toHexCharArray(keyPair.pub(), 0, keyPair.pub().size))

            val inboundTopic = "$serverSignPubKeyHex/+/rx"
            println("Subscribing to $inboundTopic")
            client.subscribe(inboundTopic) { topic, message ->
                val discriminator = topic.split("/")[1]

                if (sessions.containsKey(discriminator)) {
                    val context = sessions[discriminator]!!
                    context.push(message.payload)
                } else {
                    val newContext = SingleSessionContext(keyPair, scope) { payload ->
                        val txTopic = "$serverSignPubKeyHex/$discriminator/tx"
                        val outboundMessage = MqttMessage(payload)
                        outboundMessage.qos = 2
                        client.publish(txTopic, outboundMessage)
                    }
                    cleanupSessions()
                    sessions[discriminator] = newContext
                    newContext.push(message.payload)
                    println(sessions)
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