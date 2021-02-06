package eu.geekhome.services.automation;

import java.util.Calendar;

public class BlockAutomationUnit extends EvaluableAutomationUnit {

    private final String _id;
    private final Block _block;
    private final MasterAutomation _masterAutomation;

    public Block getBlock() {
        return _block;
    }

    public BlockAutomationUnit(String id, Block block, MasterAutomation masterAutomation) {
        _id = id;
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

    public String getId() {
        return _id;
    }
}