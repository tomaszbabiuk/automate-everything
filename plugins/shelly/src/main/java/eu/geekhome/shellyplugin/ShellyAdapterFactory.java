package eu.geekhome.shellyplugin;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import eu.geekhome.services.mqtt.MqttBrokerService;

import java.util.ArrayList;

class ShellyAdapterFactory implements IHardwareManagerAdapterFactory {

    private final MqttBrokerService _mqttBroker;

    ShellyAdapterFactory(MqttBrokerService mqttBroker) {
        _mqttBroker = mqttBroker;
    }

    @Override
    public ArrayList<? extends IHardwareManagerAdapter> createAdapters() {
        ArrayList<IHardwareManagerAdapter> result = new ArrayList<>();
        ShellyAdapter adapter = new ShellyAdapter(_mqttBroker);
        result.add(adapter);

        return result;
    }
}