package com.geekhome.common.automation;

import com.geekhome.common.INamedObject;

public interface IBlocksTargetAutomationUnit<R> extends INamedObject {
    void updateEvaluationsTable(BlockAutomationUnit block, String targetParameter);
}

