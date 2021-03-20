package eu.geekhome.automation

import eu.geekhome.automation.blocks.ComparisonOperator
import eu.geekhome.services.hardware.PortValue
import java.util.*

class ComparisonAutomationNode(
    private val leftValue: IValueNode?,
    private val operator: ComparisonOperator,
    private val rightValue: IValueNode?
) : IEvaluatorNode {

    override fun evaluate(now: Calendar): Boolean {

        var leftValue: PortValue? = null
        var rightValue: PortValue? = null
        if (this.leftValue != null) {
            leftValue = this.leftValue.calculate(now)
        }

        if (this.rightValue != null) {
            rightValue = this.rightValue.calculate(now)
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