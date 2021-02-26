package eu.geekhome.automation

import eu.geekhome.services.automation.StateDeviceAutomationUnit
import eu.geekhome.services.configurable.ConditionConfigurable
import eu.geekhome.services.configurable.StateDeviceConfigurable
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

class AutomationPack(val masterNodes: List<AutomationNode>)


class BlocklyTransformer {

    fun transform(bLocklyXml: BLocklyXml, context: AutomationContext) : AutomationPack {
        val masterNodes = ArrayList<AutomationNode>()

        bLocklyXml.blocks.forEach {
            val masterNode = transformStatement(it, context)
            if (masterNode != null) {
                masterNodes.add(masterNode)
            }
        }

        return AutomationPack(masterNodes)
    }

    private fun transformValue(block: Block, context: AutomationContext) : ValueNode? {
        if (block.type.startsWith("condition_")) {
            return transformCondition(block, context)
        }

        if (block.type.startsWith("logic_and")) {
            return transformAnd(block, context)
        }

        throw UnknownBlockException(block.type)
    }

    private fun transformStatement(block: Block, context: AutomationContext) : StatementNode? {
        var next: StatementNode? = null
        if (block.next != null) {
            next = transformStatement(block.next.block!!, context)
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

        throw UnknownBlockException(block.type)
    }

    private fun transformTimeloopTrigger(
        block: Block,
        next: StatementNode?,
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
        next: StatementNode?,
        context: AutomationContext
    ): IfThanElseAutomationNode {
        var ifNode: StatementNode? = null
        var elseNode: StatementNode? = null

        if (block.statements != null) {
            val ifStatement = block.statements.find { it.name == "IF" }
            if (ifStatement == null) {
                throw MalformedBlockException(block.type, "should have <IF statement> defined")
            } else if (ifStatement.block != null) {
                ifNode = transformStatement(ifStatement.block, context)
            }

            val elseStatement = block.statements.find { it.name == "IF" }
            if (elseStatement == null) {
                throw MalformedBlockException(block.type, "should have <IF statement> defined")
            } else if (elseStatement.block != null) {
                elseNode = transformStatement(elseStatement.block, context)
            }
        }

        var valueNode: ValueNode? = null
        if (block.values == null) {
            throw MalformedBlockException(block.type, "should have at least <VALUE> defined")
        }

        if (block.values.size != 1) {
            throw MalformedBlockException(block.type, "should have only one <VALUE>")
        }

        valueNode = transformValue(block.values[0].block!!, context)

        return IfThanElseAutomationNode(next, valueNode, ifNode, elseNode)
    }

    private fun transformChangeState(
        block: Block,
        next: StatementNode?,
        context: AutomationContext
    ): ChangeStateAutomationNode {
        val state = block.type.replace("change_state_", "")
        if (context.configurable is StateDeviceConfigurable) {
            val evaluator = context.configurable.buildEvaluator(context.instanceDto, context.portFinder)
            if (evaluator is StateDeviceAutomationUnit) {
                return ChangeStateAutomationNode(state, evaluator, next)
            } else {
                throw MalformedBlockException(block.type, "should point only to a state device")
            }
        }

        throw MalformedBlockException(block.type, "it's impossible to connect this block with correct ${StateDeviceConfigurable::class.java}")
    }

    private fun transformCondition(
        block: Block,
        context: AutomationContext
    ): ConditionAutomationNode {
        val conditionId = block.type.replace("condition_", "").toLong()

        if (context.configurable is ConditionConfigurable) {
            val evaluator = context.configurable.buildEvaluator(context.instanceDto)
            return ConditionAutomationNode(evaluator)
        }

        throw MalformedBlockException(block.type, "it's impossible to connect this block with correct ${ConditionConfigurable::class.java}")
    }

    private fun transformAnd(
        block: Block,
        context: AutomationContext
    ): AndAutomationNode {
        if (block.values == null || block.values.size != 2) {
            throw MalformedBlockException(block.type, "should have exactly two <VALUE> defined")
        }

        var firstNode: ValueNode? = null
        val firstValue = block.values.find { it.name == "FIRST" }
        if (firstValue == null) {
            throw MalformedBlockException(block.type, "should have <value name=\"FIRST\"> defined")
        } else if (firstValue.block != null) {
            firstNode = transformValue(firstValue.block, context)
        }

        var secondNode: ValueNode? = null
        val secondValue = block.values.find { it.name == "SECOND" }
        if (secondValue == null) {
            throw MalformedBlockException(block.type, "should have <value name=\"SECOND\"> defined")
        } else if (secondValue.block != null) {
            secondNode = transformValue(secondValue.block, context)
        }

        //<?xml version="1.0" encoding="UTF-8"?>
        //<xml xmlns="https://developers.google.com/blockly/xml">
        //   <block type="logic_and" id="J]$:]=`R)^Q)e5_rqC1R" x="137" y="138">
        //      <value name="FIRST">
        //         <block type="condition_3" id="|Sbb21,)TWbBFzgf2Zop" />
        //      </value>
        //      <value name="SECOND">
        //         <block type="condition_3" id="QDdt]O/9`z9BauO_l88-" />
        //      </value>
        //   </block>
        //</xml>
        if (context.configurable is ConditionConfigurable) {
            val evaluator = context.configurable.buildEvaluator(context.instanceDto)
            return AndAutomationNode(firstNode, secondNode)
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