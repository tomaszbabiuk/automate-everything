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

class MqttByteChannel(private val brokerAddress: String, private val topic: String, clientId: String) : ByteChannel, IMqttMessageListener {
    private val q = LinkedBlockingQueue <ByteArray>()
    var qos = 2
    private val client = MqttClient(brokerAddress, clientId, MemoryPersistence())

    fun establishConnection() {
        connect()
        subscribe()
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

    private fun subscribe() {
        println("Subscribing to: $topic")
        client.subscribe(topic, this)

        publish(topic, byteArrayOf())
    }

    private fun publish(topic: String, content: ByteArray) {
        println("Publishing message: $content")
        val message = MqttMessage(content)
        message.qos = qos
        client.publish(topic, message)
        println("Message published")
    }

    private fun disconnect() {
        client.disconnect()
    }

    override fun read(): ByteArray {
        return q.take()
    }

    override fun write(vararg messages: ByteArray) {
        write(isLast = false, messages = messages)
    }

    override fun write(isLast: Boolean, vararg messages: ByteArray) {
        messages.forEach {
            publish(topic, it)
        }

        if (isLast) {
            disconnect()
        }
    }

    override fun messageArrived(topic: String, message: MqttMessage) {
        if (topic == this.topic) {
            q.add(message.payload)
        }
        message.payload
    }
}