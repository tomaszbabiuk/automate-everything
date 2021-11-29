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