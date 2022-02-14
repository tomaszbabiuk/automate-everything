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

package eu.automateeverything.mobileaccessplugin


import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import saltchannel.ByteChannel
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class MqttByteChannel(brokerAddress: String,
                      topic: String,
                      clientId: String)
    : ByteChannel, IMqttMessageListener {

    private val qos = 2
    private val queue = LinkedBlockingQueue <ByteArray>()
    private val client = MqttClient(brokerAddress, clientId, MemoryPersistence())

    private val rxTopic = "$topic-rx"
    private val txTopic = "$topic-tx"
    private val syncTopic = "$topic-sync"


    fun establishConnection(cancellationToken: AtomicBoolean) {
        connect()
        subscribe(cancellationToken)
    }

    private fun connect() {
        val options = MqttConnectOptions()
        options.isAutomaticReconnect = true
        options.isCleanSession = true
        options.connectionTimeout = 10
        println("Connecting")
        client.connect(options)
        println("Connected")
    }

    private fun subscribe(cancellationToken: AtomicBoolean) {
        println("Subscribing to: $rxTopic")
        client.subscribe(rxTopic, this)
        client.subscribe(syncTopic) { topic, message ->
            println("channel shold be abandoned")
            cancellationToken.set(true)
        }
    }

    private fun publish(content: ByteArray) {
        println("Publishing message: $content")
        val message = MqttMessage(content)
        message.qos = qos
        client.publish(txTopic, message)
        println("Message published")
    }

    fun disconnect() {
        try {
            client.disconnect()
        } catch (ex: Exception) {
            println(ex.toString())
        }
    }

    override fun read(cancellationToken: AtomicBoolean, debugMessage: String): ByteArray {
        println("Reading $debugMessage")
        while (!cancellationToken.get()) {
            val bytes =  queue.poll(1, TimeUnit.SECONDS)
            if (bytes != null) {
                return bytes
            }
        }

        throw Exception("Cancelled")
    }

    override fun write(vararg messages: ByteArray) {
        write(isLast = false, messages = messages)
    }

    override fun write(isLast: Boolean, vararg messages: ByteArray) {
        messages.forEach {
            publish(it)
        }
    }

    override fun messageArrived(topic: String, message: MqttMessage) {
        if (topic == this.rxTopic) {
            queue.add(message.payload)
        }
        message.payload
    }
}