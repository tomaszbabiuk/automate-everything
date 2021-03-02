package eu.geekhome.automation

import java.util.*
import kotlin.collections.ArrayList

interface AutomationNode

interface StatementNode : AutomationNode {
    val next: StatementNode?
    fun process(now: Calendar)
}

interface ValueNode: AutomationNode {
    fun evaluate(now: Calendar) : Boolean
}

class AutomationPack(val triggers: List<StatementNode>)

interface IBlocklyTransformer {

    fun transformValue(block: Block, context: AutomationContext) : ValueNode
    fun transformTrigger(block: Block, context: AutomationContext) : StatementNode
    fun transformStatement(block: Block, context: AutomationContext) : StatementNode
}

class BlocklyTransformer() : IBlocklyTransformer {

    fun transform(bLocklyXml: BLocklyXml, context: AutomationContext) : AutomationPack {

        val masterNodes = ArrayList<StatementNode>()

        bLocklyXml.blocks.forEach {
            val masterNode = transformTrigger(it, context)
            masterNodes.add(masterNode)
        }

        return AutomationPack(masterNodes)
    }

    override fun transformValue(block: Block, context: AutomationContext) : ValueNode {
        val blockFactory = context
            .blocksCache
            .filterIsInstance<ValueBlockFactory>()
            .find { it.match(block.type) }

        if (blockFactory != null) {
            return blockFactory.transform(block, null, context, this)
        }

        throw UnknownValueBlockException(block.type)
    }

    override fun transformTrigger(block: Block, context: AutomationContext) : StatementNode {
        var next: StatementNode? = null
        if (block.next != null) {
            next = transformStatement(block.next.block!!, context)
        }

        val blockFactory = context
            .blocksCache
            .filterIsInstance<TriggerBlockFactory>()
            .find { it.match(block.type) }

        if (blockFactory != null) {
            return blockFactory.transform(block, next, context, this)
        }

        throw UnknownTriggerBlockException(block.type)
    }

    override fun transformStatement(block: Block, context: AutomationContext) : StatementNode {
        var next: StatementNode? = null
        if (block.next != null) {
            next = transformStatement(block.next.block!!, context)
        }

        val blockFactory = context
            .blocksCache
            .filterIsInstance<StatementBlockFactory>()
            .find { it.match(block.type) }

        if (blockFactory != null) {
            return blockFactory.transform(block, next, context, this)
        }

        throw UnknownStatementBlockException(block.type)
    }

}

class MalformedBlockException(type: String, malfunction: String) : Exception(
    "Malformed block $type: $malfunction"
)

open class UnknownBlockException(message:String) : Exception(message)

class UnknownTriggerBlockException(type: String) : UnknownBlockException(
    "Unknown trigger block: $type"
)

class UnknownStatementBlockException(type: String) : UnknownBlockException(
    "Unknown statement block: $type"
)

class UnknownValueBlockException(type: String) : UnknownBlockException(
    "Unknown value block: $type"
)