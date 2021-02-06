package eu.geekhome.services.automation;

import java.util.Calendar;

public class AutomationCycler {
    private boolean _dryRun;
    private boolean _enabled;

    public void setAutomationEnabled(boolean value) {
        _enabled = value;
    }

    protected boolean isAutomationEnabled() {
        return _enabled;
    }

    private MasterAutomation _masterAutomation;

    public AutomationCycler(MasterAutomation masterAutomation, boolean enabled) {
        _masterAutomation = masterAutomation;
        _enabled = enabled;
        _dryRun = true;
    }

    public void automate(Calendar now, Runnable afterLoopCallback) throws Exception {
        if (isAutomationEnabled()) {
            doAutomationCycle(now);
            if (_dryRun) {
                doAutomationCycle(now);
                _dryRun = false;
            }

            if (afterLoopCallback != null) {
                afterLoopCallback.run();
            }
        } else {
            _dryRun = true;
        }
    }


    private void doAutomationCycle(Calendar now) throws Exception {
        if (_enabled) {
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
