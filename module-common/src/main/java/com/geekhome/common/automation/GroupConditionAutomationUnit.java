package com.geekhome.common.automation;

import com.geekhome.common.configuration.GroupCondition;
import com.geekhome.common.configuration.GroupMatch;

import java.util.Calendar;

public class GroupConditionAutomationUnit extends EvaluableAutomationUnit {
    private GroupCondition _condition;
    private MasterAutomation _masterAutomation;

    public GroupConditionAutomationUnit(GroupCondition condition, MasterAutomation masterAutomation) {
        super(condition.getName());
        _condition = condition;
        _masterAutomation = masterAutomation;
    }

    private boolean allConditionsPassed(Calendar now) throws Exception {
        if (!_condition.getConditionsIds().equals("")) {
            for (String conditionId : _condition.getConditionsIds().split(",")) {
                boolean isNegation = conditionId.startsWith("!");
                if (isNegation) {
                    conditionId = conditionId.substring(1);
                }
                IEvaluableAutomationUnit unit =_masterAutomation.findConditionUnit(conditionId);

                if ((!isNegation && !unit.evaluate(now)) || (isNegation && unit.evaluate(now))) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    private boolean anyConditionPassed(Calendar now) throws Exception {
        if (!_condition.getConditionsIds().equals("")) {
            for (String conditionId : _condition.getConditionsIds().split(",")) {
                boolean isNegation = conditionId.startsWith("!");
                if (isNegation) {
                    conditionId = conditionId.substring(1);
                }
                IEvaluableAutomationUnit unit = _masterAutomation.findConditionUnit(conditionId);

                if ((!isNegation && unit.evaluate(now)) || (isNegation && !unit.evaluate(now))) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected boolean doEvaluate(Calendar now) throws Exception {
        if (_condition.getMatch() == GroupMatch.AllInTheGroup) {
            return allConditionsPassed(now);
        }

        return anyConditionPassed(now);
    }
}
