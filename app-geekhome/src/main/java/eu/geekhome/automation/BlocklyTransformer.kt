package eu.geekhome.automation

import eu.geekhome.services.configurable.ConditionConfigurable
import eu.geekhome.services.configurable.StateDeviceConfigurable

interface AutomationNode {
    val next: AutomationNode?
    fun process(timeInMillis: Long)
}

class AutomationPack(val masterNodes: List<AutomationNode>)


class BlocklyTransformer {

    fun transform(bLocklyXml: BLocklyXml, context: AutomationContext) : AutomationPack {
        val masterNodes = ArrayList<AutomationNode>()

        bLocklyXml.blocks.forEach {
            val masterNode = transform(it, context)
            if (masterNode != null) {
                masterNodes.add(masterNode)
            }
        }

        return AutomationPack(masterNodes)
    }

    private fun transform(block: Block, context: AutomationContext) : AutomationNode? {
        var next: AutomationNode? = null
        if (block.next != null) {
            next = transform(block.next.block!!, context)
        }

        if (block.type == "trigger_timeloop") {
            return transformTimeloopTrigger(block, next)
        }

        if (block.type == "logic_if_than_else") {
            return transformIfThanElse(block, next, context)
        }

        if (block.type.startsWith("change_state_")) {
            return transformChangeState(block, next, context)
        }

        if (block.type.startsWith("condition_")) {
            return transformCondition(block, next, context)
        }

        throw UnknownBlockException(block.type)
    }

    private fun transformTimeloopTrigger(
        block: Block,
        next: AutomationNode?,
    ): TimeTriggerBlock {
        if (block.field == null) {
            throw MalformedBlockException(block.type, "should have <field> defined")
        }
        if (block.field.value == null) {
            throw MalformedBlockException(block.type, "should have <field/value> defined")
        }

        val seconds = block.field.value.toInt()

        return TimeTriggerBlock(seconds, next)
    }

    private fun transformIfThanElse(
        block: Block,
        next: AutomationNode?,
        context: AutomationContext
    ): IfThanElseAutomationNode {
        if (block.statements == null) {
            throw MalformedBlockException(block.type, "should have <statements> defined")
        }

        var ifNode: AutomationNode? = null
        val ifStatement = block.statements.find { it.name == "IF" }
        if (ifStatement == null) {
            throw MalformedBlockException(block.type, "should have <IF statement> defined")
        } else if (ifStatement.block != null) {
            ifNode = transform(ifStatement.block, context)
        }

        var elseNode: AutomationNode? = null
        val elseStatement = block.statements.find { it.name == "IF" }
        if (elseStatement == null) {
            throw MalformedBlockException(block.type, "should have <IF statement> defined")
        } else if (elseStatement.block != null) {
            elseNode = transform(elseStatement.block, context)
        }

        return IfThanElseAutomationNode(next, ifNode, elseNode)
    }

    private fun transformChangeState(
        block: Block,
        next: AutomationNode?,
        context: AutomationContext
    ): ChangeStateAutomationNode {
        val state = block.type.replace("change_state_", "")
        if (context.configurable is StateDeviceConfigurable) {
            val evaluator = context.configurable.buildEvaluator(context.instanceDto, context.portFinder)
            return ChangeStateAutomationNode(state, evaluator, next)
        }

        throw MalformedBlockException(block.type, "it's impossible to connect this block with correct ${StateDeviceConfigurable::class.java}")
    }

    private fun transformCondition(
        block: Block,
        next: AutomationNode?,
        context: AutomationContext
    ): ConditionAutomationNode {
        val conditionId = block.type.replace("condition_", "").toLong()
        if (context.configurable is ConditionConfigurable) {
            val evaluator = context.configurable.buildEvaluator(context.instanceDto)
            return ConditionAutomationNode(conditionId, evaluator, next)
        }

        throw MalformedBlockException(block.type, "it's impossible to connect this block with correct ${ConditionConfigurable::class.java}")
    }
}

class MalformedBlockException(type: String, malfunction: String) : Exception(
    "Malformed block $type: $malfunction"
)

class UnknownBlockException(type: String) : Exception(
    "Unknown block: $type"
)