package eu.automateeverything.onewireplugin;

import com.dalsemi.onewire.OneWireException;
import com.dalsemi.onewire.adapter.DSPortAdapter;
import com.dalsemi.onewire.adapter.USerialAdapter;
import com.dalsemi.onewire.container.OneWireContainer;
import com.dalsemi.onewire.container.OneWireContainer01;
import com.dalsemi.onewire.container.OneWireContainer28;
import com.dalsemi.onewire.container.SwitchContainer;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.Callable;

class DiscoveryProcess implements Callable<ArrayList<DiscoveryInfo>> {
    private final String _serialPortName;
    private USerialAdapter _adapter;

    DiscoveryProcess(String serialPortName) {
        _serialPortName = serialPortName;
    }

    private void free() throws OneWireException {
        _adapter.freePort();
    }

    private USerialAdapter initializeAdapter(String port) throws OneWireException {
        USerialAdapter adapter = new USerialAdapter();
        adapter.selectPort(port);
        adapter.reset();
        return adapter;
    }

    @Override
    public ArrayList<DiscoveryInfo> call() throws Exception {
        ArrayList<DiscoveryInfo> discovered = new ArrayList<>();
        _adapter = initializeAdapter(_serialPortName);
        _adapter.beginExclusive(true);
        _adapter.setSearchAllDevices();
        _adapter.targetAllFamilies();
        _adapter.setSpeed(DSPortAdapter.SPEED_REGULAR);

        OneWireContainer owd;
        for (Enumeration owd_enum = _adapter.getAllDeviceContainers(); owd_enum.hasMoreElements(); ) {
            owd = (OneWireContainer) owd_enum.nextElement();
            DiscoveryInfo discoveryInfo = null;
            if (owd instanceof SwitchContainer) {
                discoveryInfo = new SwitchDiscoveryInfo((SwitchContainer) owd);
            } else if (owd instanceof OneWireContainer28) {
                discoveryInfo = new TemperatureDiscoveryInfo((OneWireContainer28) owd);
            } else if (owd instanceof OneWireContainer01) {
                discoveryInfo = new IdentityDiscoveryInfo((OneWireContainer01)owd);
            }

            if (discoveryInfo != null) {
                discovered.add(discoveryInfo);
            }
        }
        _adapter.endExclusive();
        free();
        return discovered;
    }
}
