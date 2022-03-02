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


import saltchannel.ByteChannel
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class ChannelTerminatedException: Exception("Mqtt channel has been terminated!")

class QueuedCancellableByteChannel(
    private val cancellationToken: AtomicBoolean,
    private val writer: (ByteArray) -> Unit
) : ByteChannel {

    private val queue = LinkedBlockingQueue <ByteArray>()

    fun offer(data: ByteArray) {
        queue.offer(data)
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
            writer.invoke(it)
        }
    }
}