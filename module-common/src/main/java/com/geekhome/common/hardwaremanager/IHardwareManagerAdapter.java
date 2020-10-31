package com.geekhome.common.hardwaremanager;

import com.geekhome.common.DiscoveryException;
import com.geekhome.common.IMonitorable;
import com.geekhome.common.INamedObject;
import com.geekhome.common.RefreshState;
import com.geekhome.common.OperationMode;

import java.util.Calendar;

public interface IHardwareManagerAdapter extends IMonitorable, INamedObject {
    void discover(InputPortsCollection<Boolean> digitalInputPorts, OutputPortsCollection<Boolean> digitalOutputPorts,
                  InputPortsCollection<Double> powerInputPorts, OutputPortsCollection<Integer> powerOutputPorts,
                  InputPortsCollection<Double> temperaturePorts, TogglePortsCollection togglePorts,
                  InputPortsCollection<Double> humidityPorts, InputPortsCollection<Double> luminosityPorts) throws DiscoveryException;
    boolean canBeRediscovered();
    void refresh(Calendar now) throws Exception;
    RefreshState getRefreshState();
    void resetLatches();
    void reconfigure(OperationMode operationMode) throws Exception;
    void stop();
}
