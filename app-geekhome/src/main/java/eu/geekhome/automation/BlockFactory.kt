package eu.geekhome.automation

import eu.geekhome.rest.RawJson
import eu.geekhome.services.localization.Resource

interface BlockFactory<N: AutomationNode> {
    val category: Resource
    val type:String
    fun buildBlock() : RawJson
    fun match(type: String): Boolean
    fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: IBlocklyTransformer) : N
}

//preventing type erasure
interface ValueBlockFactory: BlockFactory<ValueNode>

//preventing type erasure
interface StatementBlockFactory: BlockFactory<StatementNode>

//preventing type erasure
interface TriggerBlockFactory: BlockFactory<StatementNode>
