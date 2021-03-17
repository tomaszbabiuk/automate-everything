package eu.geekhome.automation

import eu.geekhome.services.hardware.PortValue
import eu.geekhome.services.hardware.Temperature
import java.util.*
import kotlin.collections.ArrayList

interface IAutomationNode

interface IStatementNode : IAutomationNode {
    val next: IStatementNode?
    fun process(now: Calendar)
}

interface IEvaluatorNode: IAutomationNode {
    fun evaluate(now: Calendar) : Boolean
}

interface IValueNode<T: PortValue>: IAutomationNode {
    fun calculate(now: Calendar) : T
}

class ValueNode<T: PortValue>(val value: T) : IValueNode<T> {
    override fun calculate(now: Calendar): T {
        return value
    }
}

interface IBlocklyTransformer {

    fun transformEvaluator(block: Block, context: AutomationContext) : IEvaluatorNode
    fun <T: PortValue> transformValue(block: Block, context: AutomationContext) : IValueNode<T>
    fun transformTrigger(block: Block, context: AutomationContext) : IStatementNode
    fun transformStatement(block: Block, context: AutomationContext) : IStatementNode
}

class BlocklyTransformer : IBlocklyTransformer {

    fun transform(bLocklyXml: BLocklyXml, context: AutomationContext) : List<IStatementNode> {

        val masterNodes = ArrayList<IStatementNode>()

        bLocklyXml.blocks.forEach {
            val masterNode = transformTrigger(it, context)
            masterNodes.add(masterNode)
        }

        return masterNodes
    }

    override fun transformEvaluator(block: Block, context: AutomationContext) : IEvaluatorNode {
        val blockFactory = context
            .blocksCache
            .filterIsInstance<EvaluatorBlockFactory>()
            .find { it.match(block.type) }

        if (blockFactory != null) {
            return blockFactory.transform(block, null, context, this)
        }

        throw UnknownEvaluatorBlockException(block.type)
    }

    override fun <T: PortValue> transformValue(block: Block, context: AutomationContext): IValueNode<T> {
        val blockFactory = context
            .blocksCache
            .filterIsInstance<ValueBlockFactory<T>>()
            .find { it.match(block.type) }

        if (blockFactory != null) {
            return blockFactory.transform(block, null, context, this)
        }

        throw UnknownValueBlockException(block.type)
    }

    override fun transformTrigger(block: Block, context: AutomationContext) : IStatementNode {
        var next: IStatementNode? = null
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

    override fun transformStatement(block: Block, context: AutomationContext) : IStatementNode {
        var next: IStatementNode? = null
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

class UnknownEvaluatorBlockException(type: String) : UnknownBlockException(
    "Unknown evaluatorV block: $type"
)

class UnknownValueBlockException(type: String) : UnknownBlockException(
    "Unknown value block: $type"
)