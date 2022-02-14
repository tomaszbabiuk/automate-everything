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
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import saltchannel.util.Hex
import saltchannel.util.KeyPair
import saltchannel.util.Rand
import saltchannel.v2.SaltServerSession
import java.security.SecureRandom
import java.util.concurrent.atomic.AtomicBoolean

class MqttSaltServer(
    brokerAddress: String,
    private val secretsPassword: String,
    private val inbox: Inbox,
    private val sessionHandler: ByteArraySessionHandler,
    private val channelActivator: ChannelActivator
)
    : WithStartStopScope<List<String>>() {

    private var contexts: List<SessionContext>? = null
    private val random = Rand { b -> SecureRandom.getInstanceStrong().nextBytes(b) }
    private val serviceCancellationToken = AtomicBoolean(false)

    private val client = MqttClient(brokerAddress, "Automate Everything - Mobile Access", MemoryPersistence())

    private fun connect() {
        val options = MqttConnectOptions()
        options.isAutomaticReconnect = true
        options.isCleanSession = true
        options.connectionTimeout = 10
        println("Connecting")
        client.connect(options)
        println("Connected")
    }

    override fun start(params: List<String>) {
        super.start(params)
        serviceCancellationToken.set(false)

        if (params.isNotEmpty()) {
            startStopScope.launch {
                while (!serviceCancellationToken.get()) {
                    if (!client.isConnected) {
                        connect()
                    }

                    contexts = buildContext(params)

                    val x = contexts!!
                        .map {
                            it.begin()
                            it.job!!
                        }

                    x.awaitAll()
                }
            }

            //TODO: add mqtt client connection supervisor
        }
    }

    private fun buildContext(params: List<String>): List<SessionContext> {
        val storage = SecretStorage()
        return params.mapNotNull { serverSignPubKeyHex ->
            val keyPair = storage.loadSecret(secretsPassword, serverSignPubKeyHex)
            if (keyPair != null) {
                SessionContext(keyPair)
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
        super.stop()
    }

    inner class SessionContext(val keyPair: KeyPair) {
        private val byteChannel: MqttByteChannel
        val session: SaltServerSession
        var job: Deferred<Unit>? = null
        private val connectionCancellationToken = AtomicBoolean(false)

        init {
            val serverSignPubKeyHex = String(Hex.toHexCharArray(keyPair.pub(), 0, keyPair.pub().size))
            byteChannel = MqttByteChannel(serverSignPubKeyHex, client)
            session = SaltServerSession(keyPair, byteChannel)
            session.setEncKeyPair(random)
        }

        fun cancel() {
            connectionCancellationToken.set(true)
        }

        suspend fun begin() = coroutineScope {
            job = async {
                try {
                    connectionCancellationToken.set(false)
                    byteChannel.establishConnection(connectionCancellationToken)

                    session.handshake(connectionCancellationToken)
                    channelActivator.activateChannel(keyPair.pub(), session.clientSigKey)

                    while (!connectionCancellationToken.get()) {
                        val incomingData = session.channel.read(connectionCancellationToken, "incoming packets")
                        val isLast = session.channel.lastFlag()

                        if (isLast) {
                            println("got last data")
                        } else {
                            println("got data")
                        }

                        val responses = sessionHandler.handleRequest(incomingData)
                        session.channel.write(false, responses)
                        //TODO: handle case when message is too big

                        if (isLast) {
                            break
                        }
                    }

                    println("context finishes")
                } catch (ex: Exception) {
                    println("context has failed, $ex")
                }
            }
        }
    }
}