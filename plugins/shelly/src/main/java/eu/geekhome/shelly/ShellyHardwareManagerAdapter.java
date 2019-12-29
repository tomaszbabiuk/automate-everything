package eu.geekhome.shelly;

import com.geekhome.common.DiscoveryException;
import com.geekhome.common.OperationMode;
import com.geekhome.common.RefreshState;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.extensibility.RequiresMqttFeature;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.hardwaremanager.InputPortsCollection;
import com.geekhome.common.hardwaremanager.OutputPortsCollection;
import com.geekhome.common.hardwaremanager.TogglePortsCollection;
import com.geekhome.moquettemodule.MqttBroker;
import org.pf4j.Extension;

import java.util.Calendar;

@Extension(ordinal=1)
public class ShellyHardwareManagerAdapter implements IHardwareManagerAdapter, RequiresMqttFeature {
    @Override
    public void discover(InputPortsCollection<Boolean> digitalInputPorts, OutputPortsCollection<Boolean> digitalOutputPorts, InputPortsCollection<Double> powerInputPorts, OutputPortsCollection<Integer> powerOutputPorts, InputPortsCollection<Double> temperaturePorts, TogglePortsCollection togglePorts, InputPortsCollection<Double> humidityPorts, InputPortsCollection<Double> luminosityPorts) throws DiscoveryException {
    }

    @Override
    public boolean canBeRediscovered() {
        return false;
    }

    @Override
    public void refresh(Calendar now) throws Exception {
    }

    @Override
    public RefreshState getRefreshState() {
        return null;
    }

    @Override
    public void resetLatches() {
    }

    @Override
    public void reconfigure(OperationMode operationMode) throws Exception {
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isOperational() {
        return false;
    }

    @Override
    public void start() {
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public DescriptiveName getName() {
        return null;
    }

    @Override
    public void setMqttBroker(MqttBroker broker) {

    }

    @Override
    public void allFeaturesInjected() {

    }
}

