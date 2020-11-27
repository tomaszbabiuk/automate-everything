package eu.geekhome.shellyplugin;

import com.geekhome.common.extensibility.RequiresMqttFeature;
import com.geekhome.common.hardwaremanager.*;
import eu.geekhome.services.mqtt.MqttBrokerService;
import org.pf4j.Extension;

import java.util.ArrayList;


@Extension
public class ShellyAdapterFactory implements IHardwareManagerAdapterFactory, RequiresMqttFeature {

    private MqttBrokerService _mqttBroker;

    @Override
    public void setMqttBroker(MqttBrokerService broker) {
        _mqttBroker = broker;
    }

    @Override
    public void allFeaturesInjected() {
    }

    @Override
    public ArrayList<? extends IHardwareManagerAdapter> createAdapters() {
//        ArrayList<ShellyAdapter> result = new ArrayList<>();
//        ShellyAdapter adapter = new ShellyAdapter(_mqttBroker);
//        result.add(adapter);
//        return result;

        return null;
    }
}