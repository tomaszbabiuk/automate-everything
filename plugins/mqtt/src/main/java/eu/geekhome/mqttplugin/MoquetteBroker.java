package eu.geekhome.mqttplugin;

import eu.geekhome.services.mqtt.MqttBrokerService;
import eu.geekhome.services.mqtt.MqttListener;
import io.moquette.BrokerConstants;
import io.moquette.broker.Server;
import io.moquette.broker.config.MemoryConfig;
import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.InterceptHandler;
import io.moquette.interception.messages.InterceptConnectionLostMessage;
import io.moquette.interception.messages.InterceptPublishMessage;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.pf4j.Extension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Extension
public class MoquetteBroker implements MqttBrokerService {

    private final Server _mqttBroker;
    private final List<MqttListener> _listeners = new ArrayList<>();

    @Override
    public void addMqttListener(MqttListener listener) {
        _listeners.add(listener);
    }

    @Override
    public void removeMqttListener(MqttListener listener) {
        _listeners.remove(listener);
    }

    public MoquetteBroker() {
        _mqttBroker = new Server();
    }

    static class PublisherListener extends AbstractInterceptHandler {

        private final List<MqttListener> _listeners;

        PublisherListener(List<MqttListener> listeners) {
            _listeners = listeners;
        }

        @Override
        public String getID() {
            return "EmbeddedLauncherPublishListener";
        }

        @Override
        public void onPublish(InterceptPublishMessage msg) {

            int size = 0;
            byte[] buffer = new byte[255];
            while (msg.getPayload().isReadable()) {
                buffer[size] = msg.getPayload().readByte();
                size++;
            }
            String msgAsString = new String(buffer, 0, size);

            System.out.println("Received on topic: " + msg.getTopicName() + ", content: " + msgAsString);

            for (MqttListener listener : _listeners) {
                listener.onPublish(msg.getTopicName(), msgAsString);
            }
        }

        @Override
        public void onConnectionLost(InterceptConnectionLostMessage msg) {
            String clientID = msg.getClientID();
            for (MqttListener listener : _listeners) {
                listener.onDisconnected(clientID);
            }
        }
    }

    public void start() throws IOException {
        List<? extends InterceptHandler> userHandlers = Collections.singletonList(new PublisherListener(_listeners));

        MemoryConfig memoryConfig = new MemoryConfig(new Properties());
        memoryConfig.setProperty(BrokerConstants.ALLOW_ANONYMOUS_PROPERTY_NAME, "true");

        _mqttBroker.startServer(memoryConfig, userHandlers);
    }

    public void stop() {
        _mqttBroker.stopServer();
    }

    @Override
    public void publish(String topic, String content) {
        MqttPublishMessage message = MqttMessageBuilders.publish()
                .topicName(topic)
                .retained(true)
                .qos(MqttQoS.EXACTLY_ONCE)
                .payload(Unpooled.copiedBuffer(content.getBytes()))
                .build();

        _mqttBroker.internalPublish(message, "geekHOME Server 2");
    }
}
