package eu.automateeverything.domain.automation

import eu.automateeverything.domain.automation.blocks.ComparisonOperator
import eu.automateeverything.domain.hardware.PortValue
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
                ComparisonOperator.Greater -> leftValue.asDouble() > rightValue.asDouble()
                ComparisonOperator.Lesser -> leftValue.asDouble() < rightValue.asDouble()
                ComparisonOperator.Equals -> leftValue.asDouble() == rightValue.asDouble()
                ComparisonOperator.NotEquals -> leftValue.asDouble() != rightValue.asDouble()
                ComparisonOperator.GreaterOrEqual -> leftValue.asDouble() >= rightValue.asDouble()
                ComparisonOperator.LesserOrEqual -> leftValue.asDouble() <= rightValue.asDouble()
            }
        }

        return false
    }
}