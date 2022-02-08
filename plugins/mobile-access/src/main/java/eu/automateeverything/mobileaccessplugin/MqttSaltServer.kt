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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import saltchannel.util.Rand
import saltchannel.v2.SaltServerSession
import java.security.SecureRandom

class MqttSaltServer(
    private val brokerAddress: String,
    private val secretsPassword: String,
    private val inbox: Inbox)
    : WithStartStopScope<List<String>>() {

    private val random = Rand { b -> SecureRandom.getInstanceStrong().nextBytes(b) }

    @OptIn(ExperimentalSerializationApi::class)
    override fun start(params: List<String>) {
        super.start(params)
        startStopScope.launch {
            val storage = SecretStorage()
            val clientSessions = params
                .map {
                    val keyPair = storage.loadSecret(secretsPassword, it)
                    if (keyPair != null) {
                        val byteChannel = MqttByteChannel(brokerAddress, it, it)
                        byteChannel.establishConnection()
                        SaltServerSession(keyPair, byteChannel)
                    } else {
                        inbox.sendMessage(R.inbox_message_mqtt_server_error_subject, R.inbox_message_mqtt_server_error_missing_keypair)
                        null
                    }
                }

            val notNullSessions = clientSessions.filterNotNull()


            notNullSessions
                .forEach {
                    it.setEncKeyPair(random)
                    it.handshake()
                    println("Session successfully open with ${it.clientSigKey}")
                    //TODO: Move that to separate jobs
                    while (!it.isDone) {
                        val incomingData = it.channel.read()
                        delay(10)
                        val data = Cbor.decodeFromByteArray<HttpRequestWrapper>(incomingData)
                        println(data)
                    }
                    println("Session is done")
            }
        }
    }
}