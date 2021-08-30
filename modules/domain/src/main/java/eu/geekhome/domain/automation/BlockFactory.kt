package eu.geekhome.domain.automation

import eu.geekhome.data.blocks.RawJson
import eu.geekhome.domain.automation.blocks.BlockCategory

interface BlockFactory<N: IAutomationNode> {
    val category: BlockCategory
    val type:String
    fun buildBlock() : RawJson
    fun transform(block: Block, next: IStatementNode?, context: AutomationContext, transformer: IBlocklyTransformer) : N
}

//preventing type erasure
interface EvaluatorBlockFactory: BlockFactory<IEvaluatorNode>

//preventing type erasure
interface ValueBlockFactory: BlockFactory<IValueNode>

//preventing type erasure
interface StatementBlockFactory: BlockFactory<IStatementNode>

//preventing type erasure
interface TriggerBlockFactory: BlockFactory<IStatementNode>
