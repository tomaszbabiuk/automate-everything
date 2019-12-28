package eu.geekhome.shelly;

import com.geekhome.common.DiscoveryException;
import com.geekhome.common.OperationMode;
import com.geekhome.common.RefreshState;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.hardwaremanager.InputPortsCollection;
import com.geekhome.common.hardwaremanager.OutputPortsCollection;
import com.geekhome.common.hardwaremanager.TogglePortsCollection;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import eu.geekhome.Greeting;

import java.util.Calendar;

public class HelloPlugin extends Plugin {

    public HelloPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("Shelly plugin.start()");
    }

    @Override
    public void stop() {
        System.out.println("Shelly plugin.stop()");
    }

    @Extension(ordinal=1)
    public static class ShellyHardwareManagerAdapter implements IHardwareManagerAdapter {
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
    }

}
