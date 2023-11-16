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

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.blocks.BlockCategory

interface BlockFactory<N : AutomationNode, C> {
    val category: BlockCategory
    val type: String

    fun buildBlock(): RawJson

    fun transform(
        block: Block,
        next: StatementNode?,
        context: C,
        transformer: BlocklyTransformer,
        order: Int = 0
    ): N

    fun dependsOn(): List<Long> = listOf()

    fun belongs(type: String): Boolean = this.type == type
}

// preventing type erasure
interface EvaluatorBlockFactory : BlockFactory<EvaluatorNode, AutomationContext>

// preventing type erasure
interface ValueBlockFactory : BlockFactory<ValueNode, AutomationContext>

// preventing type erasure
interface StatementBlockFactory : BlockFactory<StatementNode, AutomationContext>

// preventing type erasure
interface TriggerBlockFactory : BlockFactory<StatementNode, AutomationContext>
