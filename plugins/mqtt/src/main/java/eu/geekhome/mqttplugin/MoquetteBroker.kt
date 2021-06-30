package eu.geekhome.mqttplugin

import eu.geekhome.domain.mqtt.MqttBrokerService
import eu.geekhome.domain.mqtt.MqttListener
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.pf4j.Extension
import java.net.UnknownHostException
import java.util.*

@Extension
class MoquetteBroker : MqttBrokerService {
    private val broker: Server = Server()
    private val listeners: MutableList<MqttListener> = ArrayList()
    var pluginScope: CoroutineScope? = null


    override fun addMqttListener(listener: MqttListener) {
        listeners.add(listener)
    }

    override fun removeMqttListener(listener: MqttListener) {
        listeners.remove(listener)
    }

    internal class PublisherListener(
        private val pluginScope: CoroutineScope,
        private val listeners: List<MqttListener>,
        private val server: Server
    ) :
        AbstractInterceptHandler() {
        override fun getID(): String {
            return "EmbeddedLauncherPublishListener"
        }

        override fun onPublish(msg: InterceptPublishMessage) {
            var size = 0
            val buffer = ByteArray(255)
            while (msg.payload.isReadable) {
                buffer[size] = msg.payload.readByte()
                size++
            }
            val msgAsString = String(buffer, 0, size)
            println("Received on topic: " + msg.topicName + ", content: " + msgAsString)
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
            pluginScope.launch {
                for (client in server.listConnectedClients()) {
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
    fun start() {
        if (pluginScope != null) {
            pluginScope?.cancel("MQTT Broker already started")
        }
        pluginScope = CoroutineScope(Dispatchers.IO)
        val userHandlers: List<InterceptHandler?> = listOf(PublisherListener(pluginScope!!, listeners, broker))
        val memoryConfig = MemoryConfig(Properties())
        memoryConfig.setProperty(BrokerConstants.ALLOW_ANONYMOUS_PROPERTY_NAME, "true")
        broker.startServer(memoryConfig, userHandlers)
    }

    fun stop() {
        pluginScope?.cancel("MQTT Broker has been stopped")
        pluginScope = null
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