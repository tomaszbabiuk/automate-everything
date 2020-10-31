package com.geekhome.common.automation;

import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.OperationMode;
import java.util.Calendar;

public class AutomationCycler {
    private boolean _dryRun;
    private ILogger _logger = LoggingService.getLogger();
    private boolean _enabled;
    private long _than;

    public void setAutomationEnabled(boolean value) {
        _than = Calendar.getInstance().getTimeInMillis();
        _enabled = value;
    }

    protected boolean isAutomationEnabled() {
        return _enabled;
    }

    private MasterAutomation _masterAutomation;
    private SystemInfo _systemInfo;

    public AutomationCycler(MasterAutomation masterAutomation, SystemInfo systemInfo) {
        _masterAutomation = masterAutomation;
        _systemInfo = systemInfo;
        _dryRun = true;
    }

    public void automate(Calendar now, Runnable afterLoopCallback) throws Exception {
        if (isAutomationEnabled()) {
            doAutomationCycle(now);
            if (_dryRun) {
                doAutomationCycle(now);
                _dryRun = false;
            }
            calculateAutomationTime(now);

            if (afterLoopCallback != null) {
                afterLoopCallback.run();
            }
        } else {
            _dryRun = true;
        }
    }

    private void calculateAutomationTime(Calendar now) {
        long nowInMillis = now.getTimeInMillis();
        if (_than == 0) {
            _than = Calendar.getInstance().getTimeInMillis();
        }
        long automationMillis = nowInMillis - _than;
        _than = nowInMillis;
        _logger.verbose(String.format("Automation calculation time [ms] = %d", automationMillis));
    }

    private void doAutomationCycle(Calendar now) throws Exception {
        if (_systemInfo.getOperationMode() == OperationMode.Automatic) {
            for (IDeviceAutomationUnit deviceUnit : _masterAutomation.getDevices().values()) {
                deviceUnit.calculate(now);
            }

            doAutomationLoop(now);
        }


    }

    private void doAutomationLoop(Calendar now) throws Exception {
        for (BlockAutomationUnit blockUnit : _masterAutomation.getBlocks().values()) {
            blockUnit.evaluate(now);

            String[] blockIdSplitted = blockUnit.getBlock().getTargetId().split("_");
            if (blockIdSplitted.length == 2) {
                String targetId = blockIdSplitted[0];
                String category = blockIdSplitted[1];
                IDeviceAutomationUnit target = _masterAutomation.findDeviceUnit(targetId, false);
                if (target != null) {
                    target.updateEvaluationsTable(blockUnit, category);
                }
            } else {
                IBlocksTargetAutomationUnit target = _masterAutomation.findBlockTargetAutomationUnit(blockIdSplitted[0]);
                target.updateEvaluationsTable(blockUnit, "default");
            }
        }

        for (IEvaluableAutomationUnit modeUnit : _masterAutomation.getModes().values()) {
            modeUnit.evaluate(now);
        }

        for (IEvaluableAutomationUnit alertUnit : _masterAutomation.getAlerts().values()) {
            alertUnit.evaluate(now);
        }
    }
}
