package eu.geekhome.shellyplugin;

import com.geekhome.common.IConnectible;
import com.geekhome.common.hardwaremanager.IPort;

public interface IShellyPort extends IConnectible, IPort {
        String getReadTopic();
        void setValueFromMqttPayload(String payload);
}
