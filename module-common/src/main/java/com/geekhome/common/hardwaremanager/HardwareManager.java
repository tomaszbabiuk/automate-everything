package com.geekhome.common.hardwaremanager;

import com.geekhome.common.*;
import com.geekhome.common.alerts.DashboardAlertService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.utils.Sleeper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class HardwareManager implements IHardwareManager {

    private interface PortIterateListener {
        void onIteratePort(IPort port);
    }

    private ILogger _logger = LoggingService.getLogger();
    private InputPortsCollection<Boolean> _digitalInputPorts;
    private OutputPortsCollection<Boolean> _digitalOutputPorts;
    private InputPortsCollection<Double> _powerInputPorts;
    private OutputPortsCollection<Integer> _powerOutputPorts;
    private InputPortsCollection<Double> _temperaturePorts;
    private InputPortsCollection<Double> _humidityPorts;
    private InputPortsCollection<Double> _luminosityPorts;
    private TogglePortsCollection _togglePorts;
    private IInvalidateCacheListener _invalidateCacheListener;
    private ArrayList<IHardwareManagerAdapter> _adapters = new ArrayList<>();
    private boolean _discoveryPending;
    private static long REDISCOVERY_TIME = 5 * 60 * 1000; //TODO: 30 * 60 * 1000
    private Long _rediscoveryTime;
    private DashboardAlertService _dashboardAlertService;

    public ArrayList<IHardwareManagerAdapter> getAdapters() {
        return _adapters;
    }

    public HardwareManager(DashboardAlertService dashboardAlertService) {
        _dashboardAlertService = dashboardAlertService;
        _digitalInputPorts = new InputPortsCollection<>(PortType.DigitalInput);
        _digitalOutputPorts = new OutputPortsCollection<>(PortType.DigitalOutput);
        _powerInputPorts = new InputPortsCollection<>(PortType.PowerInput);
        _powerOutputPorts = new OutputPortsCollection<>(PortType.PowerOutput);
        _temperaturePorts = new InputPortsCollection<>(PortType.Temperature);
        _humidityPorts = new InputPortsCollection<>(PortType.Humidity);
        _luminosityPorts = new InputPortsCollection<>(PortType.Luminosity);
        _togglePorts = new TogglePortsCollection(PortType.Toggle);
    }

    protected void clear() {
        _digitalInputPorts.clear();
        _digitalOutputPorts.clear();
        _powerInputPorts.clear();
        _powerOutputPorts.clear();
        _temperaturePorts.clear();
        _humidityPorts.clear();
        _luminosityPorts.clear();
        _togglePorts.clear();
    }

    @Override
    public OutputPortsCollection<Boolean> getDigitalOutputPorts() {
        return _digitalOutputPorts;
    }

    @Override
    public InputPortsCollection<Boolean> getDigitalInputPorts() {
        return _digitalInputPorts;
    }

    @Override
    public OutputPortsCollection<Integer> getPowerOutputPorts() {
        return _powerOutputPorts;
    }

    @Override
    public InputPortsCollection<Double> getPowerInputPorts() {
        return _powerInputPorts;
    }

    @Override
    public InputPortsCollection<Double> getTemperaturePorts() {
        return _temperaturePorts;
    }

    @Override
    public InputPortsCollection<Double> getHumidityPorts() {
        return _humidityPorts;
    }

    @Override
    public InputPortsCollection<Double> getLuminosityPorts() {
        return _luminosityPorts;
    }

    @Override
    public TogglePortsCollection getTogglePorts() {
        return _togglePorts;
    }

    @Override
    public IOutputPort<Boolean> tryFindDigitalOutputPort(String id) {
        return getDigitalOutputPorts().tryFind(id);
    }

    @Override
    public IOutputPort<Boolean> findDigitalOutputPort(String id) throws PortNotFoundException {
        return getDigitalOutputPorts().find(id);
    }

    @Override
    public IInputPort<Boolean> findDigitalInputPort(String id) throws PortNotFoundException {
        return getDigitalInputPorts().find(id);
    }

    @Override
    public IInputPort<Boolean> tryFindDigitalInputPort(String id) {
        return getDigitalInputPorts().tryFind(id);
    }

    @Override
    public IOutputPort<Integer> findPowerOutputPort(String id) throws PortNotFoundException {
        return getPowerOutputPorts().find(id);
    }

    @Override
    public IOutputPort<Integer> tryFindPowerOutputPort(String id) {
        return getPowerOutputPorts().tryFind(id);
    }

    @Override
    public IInputPort<Double> findPowerInputPort(String id) throws PortNotFoundException {
        return getPowerInputPorts().find(id);
    }

    @Override
    public IInputPort<Double> tryFindPowerInputPort(String id) {
        return getPowerInputPorts().tryFind(id);
    }

    @Override
    public IInputPort<Double> findTemperaturePort(String id) throws PortNotFoundException {
        return getTemperaturePorts().find(id);
    }

    @Override
    public IInputPort<Double> tryFindTemperaturePort(String id) {
        return getTemperaturePorts().tryFind(id);
    }

    @Override
    public IInputPort<Double> findHumidityPort(String id) throws PortNotFoundException {
        return getHumidityPorts().find(id);
    }

    @Override
    public IInputPort<Double> tryFindHumidityPort(String id) {
        return getHumidityPorts().tryFind(id);
    }

    @Override
    public IInputPort<Double> findLuminosityPort(String id) throws PortNotFoundException {
        return getLuminosityPorts().find(id);
    }

    @Override
    public IInputPort<Double> tryFindLuminosityPort(String id) {
        return getLuminosityPorts().tryFind(id);
    }

    @Override
    public ITogglePort findTogglePort(String id) throws PortNotFoundException {
        return getTogglePorts().find(id);
    }

    @Override
    public ITogglePort tryFindTogglePort(String id) {
        return getTogglePorts().tryFind(id);
    }

    @Override
    public void setPortMapper(IPortMapper portMapper) {
        initializePortMapper(portMapper);
    }

    private void initializePortMapper(IPortMapper portMapper) {
        iterateAllPorts(port -> port.initialize(portMapper));
    }

    private void iterateAllPorts(PortIterateListener listener ) {
        for (IInputPort<Boolean> port : getDigitalInputPorts().values()) {
            listener.onIteratePort(port);
        }
        for (IOutputPort<Boolean> port : getDigitalOutputPorts().values()) {
            listener.onIteratePort(port);
        }
        for (IInputPort<Double> port : getPowerInputPorts().values()) {
            listener.onIteratePort(port);
        }
        for (IOutputPort<Integer> port : getPowerOutputPorts().values()) {
            listener.onIteratePort(port);
        }
        for (IInputPort<Double> port : getTemperaturePorts().values()) {
            listener.onIteratePort(port);
        }
        for (IInputPort<Double> port : getHumidityPorts().values()) {
            listener.onIteratePort(port);
        }
        for (IInputPort<Double> port : getLuminosityPorts().values()) {
            listener.onIteratePort(port);
        }
        for (ITogglePort port : getTogglePorts().values()) {
            listener.onIteratePort(port);
        }
    }

    @Override
    public void refreshAndWait(Calendar now) throws Exception {
        int refreshTrial = 0;
        do {
            if (!_discoveryPending && _adapters != null) {
                for (IHardwareManagerAdapter adapter : _adapters) {
                    adapter.refresh(now);
                }

                waitTillAllAdaptersAreRefreshed();

                for (IHardwareManagerAdapter adapter : _adapters) {
                    adapter.resetLatches();
                    if (!adapter.isOperational()) {
                        throw new Exception("Adapter " + adapter.getName() + " non-operational");
                    }
                }
            }
            refreshTrial++;
            if (refreshTrial == 4) {
                throw new Exception("Has refresh errors despite retrying!");
            }
        } while (hasRefreshErrors());
    }

    private boolean hasRefreshErrors() {
        for (IHardwareManagerAdapter adapter : _adapters) {
            if (adapter.getRefreshState() == RefreshState.RefreshError) {
                _logger.info(String.format("Adapter refresh error found for adapter %s", adapter.toString()));
                return true;
            }
        }

        return false;
    }

    private void waitTillAllAdaptersAreRefreshed() {
        boolean refreshPending = true;
        while (refreshPending) {
            refreshPending = false;
            for (IHardwareManagerAdapter adapter : _adapters) {
                if (adapter.getRefreshState() == RefreshState.NA) {
                    continue;
                }
                if (adapter.getRefreshState() == RefreshState.Refreshing) {
                    refreshPending = true;
                    break;
                }
            }
            Sleeper.trySleep(50);
        }
    }

    public void setAdapters(ArrayList<IHardwareManagerAdapter> value) {
        _adapters = value;
    }

    @Override
    public void discover() {
        clear();

        discoverInternal(false);
    }

    @Override
    public void rediscover() {
        discoverInternal(true);
    }

    private void discoverInternal(boolean rediscovery) {
        _discoveryPending = true;

        onInvalidateCache("/DIGITALINPUTPORTS.JSON");
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        onInvalidateCache("/POWERINPUTPORTS.JSON");
        onInvalidateCache("/POWEROUTPUTPORTS.JSON");
        onInvalidateCache("/TEMPERATUREPORTS.JSON");
        onInvalidateCache("/HUMIDITYPORTS.JSON");
        onInvalidateCache("/LUMINOSITYPORTS.JSON");

        if (_adapters != null) {
            for (IHardwareManagerAdapter adapter : _adapters) {
                if (!rediscovery || adapter.canBeRediscovered())
                    try {
                        adapter.discover(
                                getDigitalInputPorts(), getDigitalOutputPorts(),
                                getPowerInputPorts(), getPowerOutputPorts(),
                                getTemperaturePorts(), getTogglePorts(),
                                getHumidityPorts(), getLuminosityPorts());

                    } catch (DiscoveryException e) {
                        _logger.error("Discovery exception", e);
                    }
            }
        }

        _discoveryPending = false;
    }

    public void invalidateAdapters(OperationMode operationMode) throws Exception {
        if (_adapters != null) {
            for (IHardwareManagerAdapter adapter : _adapters) {
                adapter.reconfigure(operationMode);
            }
        }
    }

    private void onInvalidateCache(String what) {
        if (_invalidateCacheListener != null) {
            _invalidateCacheListener.invalidate(what);
        }
    }

    @Override
    public void setInvalidateCacheListener(IInvalidateCacheListener listener) {
        _invalidateCacheListener = listener;
    }

    public void initialize(List<IHardwareManagerAdapterFactory> factories) throws Exception {
        ArrayList<IHardwareManagerAdapter> adapters = createAdapters(factories);
        setAdapters(adapters);
    }

    private ArrayList<IHardwareManagerAdapter> createAdapters(List<IHardwareManagerAdapterFactory> adapterFactories) throws Exception {
        _logger.info("Creating adapters.");
        ArrayList<IHardwareManagerAdapter> result = new ArrayList<>();

        for (IHardwareManagerAdapterFactory factory : adapterFactories) {
            ArrayList<? extends IHardwareManagerAdapter> adapters = factory.createAdapters();
            for (IHardwareManagerAdapter adapter : adapters) {
                _logger.info("Adding serial adapter " + adapter.toString());
                result.add(adapter);
            }
        }

        return result;
    }

    @Override
    public void scheduleRediscovery() {
        _rediscoveryTime = Calendar.getInstance().getTimeInMillis() + REDISCOVERY_TIME;
    }

    @Override
    public void doRediscoveryIfScheduled(Calendar now) {
        if (_rediscoveryTime != null && now.getTimeInMillis() > _rediscoveryTime) {
            if (stillGotShadows()) {
                rediscover();
                _rediscoveryTime = null;
            } else {
                _dashboardAlertService.clearShadowPortsInUse();
            }
        }
    }

    @Override
    public void scheduleRediscoveryIfUsingShadows(Calendar now) {
        if (_rediscoveryTime == null) {
            scheduleRediscovery();
        }
    }

    private boolean stillGotShadows() {
        AtomicBoolean result = new AtomicBoolean(false);
        iterateAllPorts(port -> {
            if (port instanceof IShadowPort) {
                if (!((IShadowPort)port).hasTarget()) {
                    result.set(true);
                }
            }
        });

        return result.get();
    }
}
