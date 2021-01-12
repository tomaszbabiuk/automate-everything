package eu.geekhome.services.hardware;

import com.geekhome.common.*;
import eu.geekhome.services.events.EventsSink;

import java.util.Calendar;
import java.util.List;

public interface HardwareAdapter {
    String getId();
    void discover(PortIdBuilder idBuilder, List<Port> ports, EventsSink<String> eventsSink);
    boolean canBeRediscovered();
    void refresh(Calendar now) throws Exception;
    AdapterState getState();
    void resetLatches();
    void reconfigure(OperationMode operationMode) throws Exception;
    long getLastDiscoveryTime();
    Throwable getLastError();
}
