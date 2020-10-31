package com.geekhome.common.extensibility;

import com.geekhome.moquettemodule.MqttBroker;

public interface RequiresMqttFeature extends RequiresFeature {
    void setMqttBroker(MqttBroker broker);
}
