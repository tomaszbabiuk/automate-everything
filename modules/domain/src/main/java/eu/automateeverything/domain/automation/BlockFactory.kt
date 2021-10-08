package eu.automateeverything.domain.automation

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.blocks.BlockCategory

interface BlockFactory<N: AutomationNode> {
    val category: BlockCategory
    val type:String
    fun buildBlock() : RawJson
    fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer) : N
}

//preventing type erasure
interface EvaluatorBlockFactory: BlockFactory<EvaluatorNode>

//preventing type erasure
interface ValueBlockFactory: BlockFactory<ValueNode>

//preventing type erasure
interface StatementBlockFactory: BlockFactory<StatementNode>

//preventing type erasure
interface TriggerBlockFactory: BlockFactory<StatementNode>
