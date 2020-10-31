package com.geekhome.common;


import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.localization.ILocalizationProvider;

public abstract class SerialAdapterBase extends MonitorableBase implements IHardwareManagerAdapter {
    private final ILocalizationProvider _localizationProvider;
    private ILogger _logger = LoggingService.getLogger();
    private String _serialPortName;

    protected void markAsNonOperational(Throwable ex) {
        updateStatus(false, _localizationProvider.getValue("C:AdapterError"));
        _logger.error(String.format("Adapter %s is becoming non-operational!", this.toString()), ex);
    }

    protected void markAsOperational() {
        updateStatus(true, _localizationProvider.getValue("C:Operational"));
    }

    public SerialAdapterBase(DescriptiveName name, String serialPortName, ILocalizationProvider localizationProvider) {
        super(name, true, localizationProvider.getValue("C:Initialization"));
        _localizationProvider = localizationProvider;
        _serialPortName = extractSerialPortName(serialPortName);
        _logger.info("Setting serial port to " + _serialPortName);
    }

    private String extractSerialPortName(String serialPortName) {
        if (serialPortName.contains(".")) {
            return serialPortName.substring(serialPortName.indexOf('.') + 1);
        }

        return serialPortName;
    }

    @Override
    public void reconfigure(OperationMode operationMode) throws Exception {
    }

    protected String getSerialPortName() {
        return _serialPortName;
    }
}
