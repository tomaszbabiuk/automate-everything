package com.geekhome.common.automation;

import com.geekhome.common.IMonitorable;
import com.geekhome.common.OperationMode;
import com.geekhome.common.alerts.IAlertService;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.utils.Sleeper;
import com.geekhome.common.alerts.DashboardAlertService;
import org.json.simple.JSONAware;

import java.io.*;
import java.util.Calendar;
import java.util.List;

public class SystemInfo {
    private OperationMode _operationMode;
    private IOperationModeChangedListener _operationModeChangedListener;
    private ILogger _logger = LoggingService.getLogger();
    private JSONArrayList<IAlertService> _alertServices;
    private JSONArrayList<IMonitorable> _monitorables;
    private Long _restartTime;


    @SuppressWarnings("FieldCanBeLocal")
    private static long RESTART_TIME = 5*60*1000;
    private DashboardAlertService _dashboardAlertService;

    public OperationMode getOperationMode() {
        return _operationMode;
    }

    public JSONArrayList<IAlertService> getAlertServices() { return _alertServices; }


    public void setOperationMode(OperationMode value) {
        try {
            OperationMode previousMode = _operationMode;
            _operationMode = value;
            if (previousMode != value) {
                onChanged(value);
            }
        } catch (Exception ex) {
            _operationMode = OperationMode.Diagnostics;
            _logger.error("Cannot change operation mode. Fallback to diagnostics mode", ex);
        }
    }

    public static boolean isDateReliable() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return year >= 2015;
    }

    public void setOperationModeChangedListener(IOperationModeChangedListener listener) {
        _operationModeChangedListener = listener;
    }

    private void onChanged(OperationMode operationMode) throws Exception {
        if (_operationModeChangedListener != null) {
            _operationModeChangedListener.onChanged(operationMode);
        }
    }

    public SystemInfo(OperationMode operationMode, DashboardAlertService dashboardAlertService) throws Exception {
        _dashboardAlertService = dashboardAlertService;
        _monitorables = new JSONArrayList<>();
        setOperationMode(operationMode);
    }

    public void initialize(JSONArrayList<IMonitorable> monitorables, JSONArrayList<IAlertService> alertServices) throws Exception {
        registerMonitorables(monitorables);
        registerAlertServices(alertServices);
    }

    private void registerAlertServices(JSONArrayList<IAlertService> value) { _alertServices = value; }

    private void registerMonitorables(List<IMonitorable> monitorables) {
        for (IMonitorable monitorable : monitorables) {
            if (!_monitorables.contains(monitorable)) {
                _monitorables.add(monitorable);
            }
        }
    }

    public void shutdown() throws IOException {
        setOperationMode(OperationMode.Diagnostics);

        Runnable sendShutdownSignalDelayed = new Runnable() {
            @Override
            public void run() {
                try {
                    //wait till hardware manager state change is propagated
                    Sleeper.trySleep(5000);
                    runSystemCommand("sudo shutdown -h 0");
                } catch (Exception e) {
                    _logger.error("System shutdown failed.", e);
                }
            }
        };

        Thread sendShutdownSignalThread = new Thread(sendShutdownSignalDelayed);
        sendShutdownSignalThread.start();
    }

    private void runSystemCommand(String command) throws IOException {
        Runtime r = Runtime.getRuntime();
        Process p = r.exec(command);

        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = b.readLine()) != null) {
            _logger.info("System output: " + line);
        }
    }

    public void reboot() throws IOException {
        runSystemCommand("sudo reboot");
    }

    public void scheduleAutomaticRestart() {
        _dashboardAlertService.raiseRestartAlert();
        _restartTime = Calendar.getInstance().getTimeInMillis() + RESTART_TIME;
    }

    public void doAutomaticRestartIfScheduled(Calendar now) {
        if (_restartTime != null && now.getTimeInMillis() > _restartTime) {
            try {
                reboot();
                _restartTime = null;
            } catch (IOException e) {
                _logger.error("Restart failed.", e);
            }
        }
    }

    public void startMonitorables() {
        for (IMonitorable monitorable : _monitorables) {
            _logger.info(String.format("STARTING monitorable %s", monitorable.getName()));
            monitorable.start();
            _logger.info(String.format("STARTED monitorable %s", monitorable.getName()));
        }
    }

    public JSONAware getMonitorables() {
        return _monitorables;
    }
}