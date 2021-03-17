package eu.geekhome.automation

import eu.geekhome.rest.RawJson
import eu.geekhome.services.hardware.PortValue
import eu.geekhome.services.localization.Resource

interface BlockFactory<N: IAutomationNode> {
    val category: Resource
    val type:String
    fun buildBlock() : RawJson
    fun match(type: String): Boolean
    fun transform(block: Block, next: IStatementNode?, context: AutomationContext, transformer: IBlocklyTransformer) : N
}

//preventing type erasure
interface EvaluatorBlockFactory: BlockFactory<IEvaluatorNode>

//preventing type erasure
interface ValueBlockFactory<T: PortValue>: BlockFactory<IValueNode<T>>

//preventing type erasure
interface StatementBlockFactory: BlockFactory<IStatementNode>

//preventing type erasure
interface TriggerBlockFactory: BlockFactory<IStatementNode>
