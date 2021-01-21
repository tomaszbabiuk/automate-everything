package com.geekhome.common.automation;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.NamedObject;

import java.util.Hashtable;

public class BlocksTargetAutomationUnit extends NamedObject implements IBlocksTargetAutomationUnit {
    BlocksTargetAutomationUnit(DescriptiveName name) {
        super(name);
        _evaluations = new Hashtable<>();

    }

    private final Hashtable<String, Hashtable<String, Boolean>> _evaluations;

    public void updateEvaluationsTable(BlockAutomationUnit block, String targetParameter) {
        if (!_evaluations.containsKey(targetParameter)) {
            _evaluations.put(targetParameter, new Hashtable<>());
        }

        Hashtable<String, Boolean> evaluationTable = _evaluations.get(targetParameter);
        evaluationTable.put(block.getName().getUniqueId(), block.isPassed());
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
