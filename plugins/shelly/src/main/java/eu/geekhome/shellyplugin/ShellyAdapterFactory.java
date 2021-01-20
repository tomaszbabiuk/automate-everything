package eu.geekhome.shellyplugin;

import eu.geekhome.services.hardware.HardwareAdapter;
import eu.geekhome.services.hardware.HardwareAdapterFactory;
import eu.geekhome.services.mqtt.MqttBrokerService;

import java.util.ArrayList;
import java.util.List;

class ShellyAdapterFactory implements HardwareAdapterFactory {

    public static String ID = "SHELLY";

    private final MqttBrokerService _mqttBroker;

    ShellyAdapterFactory(MqttBrokerService mqttBroker) {
        _mqttBroker = mqttBroker;
    }

    @Override
    public List<HardwareAdapter> createAdapters() {
        ArrayList<HardwareAdapter> result = new ArrayList<>();
        ShellyAdapter adapter = new ShellyAdapter(_mqttBroker);
        result.add(adapter);

        return result;
    }

    @Override
    public String getId() {
        return ID;
    }
}