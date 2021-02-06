package eu.geekhome.services.automation;

import java.util.Hashtable;

public class BlocksTargetAutomationUnit implements IBlocksTargetAutomationUnit {
    BlocksTargetAutomationUnit() {
        _evaluations = new Hashtable<>();

    }

    private Hashtable<String, Hashtable<String, Boolean>> _evaluations;

    public void updateEvaluationsTable(BlockAutomationUnit block, String targetParameter) {
        if (!_evaluations.containsKey(targetParameter)) {
            _evaluations.put(targetParameter, new Hashtable<>());
        }

        Hashtable<String, Boolean> evaluationTable = _evaluations.get(targetParameter);
        evaluationTable.put(block.getId(), block.isPassed());
    }

    protected boolean checkIfAnyBlockPasses(String category) {
        Hashtable<String, Boolean> evaluationsTable = _evaluations.get(category);
        if (evaluationsTable != null) {
            for (boolean passed : evaluationsTable.values())
            {
                if (passed) {
                    return true;
                }
            }
        }
        return false;
    }
}
