package eu.geekhome.domain.automation

import eu.geekhome.data.blocks.RawJson
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.automation.blocks.CategoryConstants

interface BlockFactory<N: IAutomationNode> {
    val category: CategoryConstants
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
