package eu.geekhome.shelly;

import com.geekhome.common.DiscoveryException;
import com.geekhome.common.OperationMode;
import com.geekhome.common.RefreshState;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.extensibility.RequiresMqttFeature;
import com.geekhome.common.hardwaremanager.*;
import com.geekhome.moquettemodule.MqttBroker;
import com.geekhome.shellymodule.ShellyAdapter;
import org.pf4j.Extension;

import java.util.ArrayList;


@Extension(ordinal=1)
public class ShellyAdapterFactory implements IHardwareManagerAdapterFactory, RequiresMqttFeature {

    private MqttBroker _mqttBroker;

    @Override
    public void setMqttBroker(MqttBroker broker) {
        _mqttBroker = broker;
    }

    @Override
    public void allFeaturesInjected() {
    }

    @Override
    public ArrayList<? extends IHardwareManagerAdapter> createAdapters() {
        ArrayList<ShellyAdapter> result = new ArrayList<>();
        ShellyAdapter adapter = new ShellyAdapter(_mqttBroker);
        result.add(adapter);
        return result;
    }
}

