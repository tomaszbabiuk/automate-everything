package eu.automateeverything.domain.automation

class BlocklyTransformer {

    fun transform(blocks: List<Block>, context: AutomationContext) : List<StatementNode> {

        val masterNodes = ArrayList<StatementNode>()

        blocks.forEach {
            val masterNode = transformTrigger(it, context)
            masterNodes.add(masterNode)
        }

        return masterNodes
    }

    fun transformEvaluator(block: Block, context: AutomationContext) : EvaluatorNode {
        val blockFactory = context
            .blocksCache
            .filterIsInstance<EvaluatorBlockFactory>()
            .find { it.type == block.type }

        if (blockFactory != null) {
            return blockFactory.transform(block, null, context, this)
        }

        throw UnknownEvaluatorBlockException(block.type)
    }

    fun transformValue(block: Block, context: AutomationContext): ValueNode {
        val blockFactory = context
            .blocksCache
            .filterIsInstance<ValueBlockFactory>()
            .find { it.type == block.type }

        if (blockFactory != null) {
            return blockFactory.transform(block, null, context, this)
        }

        throw UnknownValueBlockException(block.type)
    }

    private fun transformTrigger(block: Block, context: AutomationContext) : StatementNode {
        var next: StatementNode? = null
        if (block.next != null) {
            next = transformStatement(block.next.block!!, context)
        }

        val blockFactory = context
            .blocksCache
            .filterIsInstance<TriggerBlockFactory>()
            .find { it.type == block.type }

        if (blockFactory != null) {
            return blockFactory.transform(block, next, context, this)
        }

        throw UnknownTriggerBlockException(block.type)
    }

    fun transformStatement(block: Block, context: AutomationContext) : StatementNode {
        var next: StatementNode? = null
        if (block.next != null) {
            next = transformStatement(block.next.block!!, context)
        }

        val blockFactory = context
            .blocksCache
            .filterIsInstance<StatementBlockFactory>()
            .find { it.type == block.type }

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
    "Unknown evaluator block: $type"
)

class UnknownValueBlockException(type: String) : UnknownBlockException(
    "Unknown value block: $type"
)