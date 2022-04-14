/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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