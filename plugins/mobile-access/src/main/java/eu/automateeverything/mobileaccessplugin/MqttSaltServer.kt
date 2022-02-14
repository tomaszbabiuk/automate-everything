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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import saltchannel.util.Rand
import saltchannel.v2.SaltServerSession
import java.security.SecureRandom
import java.util.concurrent.atomic.AtomicBoolean

class MqttSaltServer(
    private val brokerAddress: String,
    private val secretsPassword: String,
    private val inbox: Inbox,
    private val sessionHandler: ByteArraySessionHandler,
    private val channelActivator: ChannelActivator
)
    : WithStartStopScope<List<String>>() {

    private val random = Rand { b -> SecureRandom.getInstanceStrong().nextBytes(b) }

    private val cancellationToken = AtomicBoolean(false)

    override fun start(params: List<String>) {
        super.start(params)

        startStopScope.launch {
            try {
                val storage = SecretStorage()
                val clientSessions = params
                    .map {
                        val keyPair = storage.loadSecret(secretsPassword, it)
                        if (keyPair != null) {
                            val byteChannel = MqttByteChannel(brokerAddress, it, it)
                            byteChannel.establishConnection()

                            Pair(keyPair.pub(), SaltServerSession(keyPair, byteChannel))
                        } else {
                            inbox.sendMessage(
                                R.inbox_message_mqtt_server_error_subject,
                                R.inbox_message_mqtt_server_error_missing_keypair
                            )
                            null
                        }
                    }

                val sessionJobs = clientSessions
                    .filterNotNull()
                    .map {
                        val serverPub = it.first
                        val serverSession = it.second
                        async {
                            serverSession.setEncKeyPair(random)
                            serverSession.handshake(cancellationToken)

                            channelActivator.activateChannel(serverPub, serverSession.clientSigKey)

                            while (true) {
                                val incomingData = serverSession.channel.read(cancellationToken)
                                delay(10)

                                val responses = sessionHandler.handleRequest(incomingData)
                                serverSession.channel.write(false, responses)

                                //TODO: maintenance part of this loop
                            }
                        }
                    }

                sessionJobs.awaitAll()
            } catch (ex:Exception) {
                println(ex)
            }
        }
    }

    override fun stop() {
        cancellationToken.set(true)
        super.stop()
    }
}