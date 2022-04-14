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

import eu.automateeverything.domain.automation.blocks.ComparisonOperator
import eu.automateeverything.data.hardware.PortValue
import java.util.*

class ComparisonAutomationNode(
    private val leftValue: ValueNode?,
    private val operator: ComparisonOperator,
    private val rightValue: ValueNode?
) : EvaluatorNode {

    override fun evaluate(now: Calendar): Boolean {

        var leftValue: PortValue? = null
        var rightValue: PortValue? = null
        if (this.leftValue != null) {
            leftValue = this.leftValue.getValue(now)
        }

        if (this.rightValue != null) {
            rightValue = this.rightValue.getValue(now)
        }

        if (leftValue != null && rightValue != null) {
            return when (operator) {
                ComparisonOperator.Greater -> leftValue.asDecimal() > rightValue.asDecimal()
                ComparisonOperator.Lesser -> leftValue.asDecimal() < rightValue.asDecimal()
                ComparisonOperator.Equals -> leftValue.asDecimal() == rightValue.asDecimal()
                ComparisonOperator.NotEquals -> leftValue.asDecimal() != rightValue.asDecimal()
                ComparisonOperator.GreaterOrEqual -> leftValue.asDecimal() >= rightValue.asDecimal()
                ComparisonOperator.LesserOrEqual -> leftValue.asDecimal() <= rightValue.asDecimal()
            }
        }

        return false
    }
}