package com.geekhome.common.automation;

import com.geekhome.common.configuration.Block;

import java.util.Calendar;

public class BlockAutomationUnit extends EvaluableAutomationUnit {

    private Block _block;
    private MasterAutomation _masterAutomation;

    public Block getBlock() {
        return _block;
    }

    public BlockAutomationUnit(Block block, MasterAutomation masterAutomation) {
        super(block.getName());
        _block = block;
        _masterAutomation = masterAutomation;
    }

    @Override
    protected boolean doEvaluate(Calendar now) throws Exception {
        for (String conditionId : _block.getConditionsIds().split(",")) {
            boolean isNegation = conditionId.startsWith("!");
            if (isNegation) {
                conditionId = conditionId.substring(1);
            }

            IEvaluableAutomationUnit unit = _masterAutomation.findConditionUnit(conditionId);
            if ((!isNegation && !unit.evaluate(now)) || (isNegation && unit.evaluate(now))) {
                return false;
            }
        }

        return true;
    }
}