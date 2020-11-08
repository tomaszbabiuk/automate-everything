package com.geekhome.common.extensibility;


import eu.geekhome.services.mqtt.MqttBrokerService;

public interface RequiresMqttFeature extends RequiresFeature {
    void setMqttBroker(MqttBrokerService broker);
}
