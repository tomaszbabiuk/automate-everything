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
import org.eclipse.paho.client.mqttv3.MqttMessage
import saltchannel.ByteChannel
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class ChannelTerminatedException: Exception("Mqtt channel has been terminated!")

class MqttByteChannel(
    topic: String,
    private val client: MqttClient,
    private val cancellationToken: AtomicBoolean)
    : ByteChannel, IMqttMessageListener {

    private val qos = 2
    private val queue = LinkedBlockingQueue <ByteArray>()

    private val rxTopic = "$topic/rx"
    private val txTopic = "$topic/tx"
    private val syncTopic = "$topic/sync"


    fun subscribe() {
        println("Subscribing to: $rxTopic")
        client.subscribe(rxTopic, this)

        client.subscribe(syncTopic) { _, message ->
            if (isResetSignal(message)) {
                cancellationToken.set(true)
            }
        }
    }

    private fun isResetSignal(message: MqttMessage) = message.payload.size == 5 &&
            message.payload[0] == 0x52.toByte() &&
            message.payload[1] == 0x45.toByte() &&
            message.payload[2] == 0x53.toByte() &&
            message.payload[3] == 0x45.toByte() &&
            message.payload[4] == 0x54.toByte()

    private fun publish(content: ByteArray) {
        println("Publishing message: $content")
        val message = MqttMessage(content)
        message.qos = qos
        client.publish(txTopic, message)
        println("Message published")
    }

    override fun read(debugMessage: String): ByteArray {
        println("Reading $debugMessage")
        while (!cancellationToken.get()) {
            val bytes =  queue.poll(1, TimeUnit.SECONDS)
            if (bytes != null) {
                return bytes
            }
        }

        throw ChannelTerminatedException()
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