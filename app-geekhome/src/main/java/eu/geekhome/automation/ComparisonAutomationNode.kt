package eu.geekhome.automation

import eu.geekhome.automation.blocks.ComparisonOperator
import eu.geekhome.services.hardware.PortValue
import java.util.*

class ComparisonAutomationNode<T: PortValue>(
    private val leftValue: IValueNode<T>?,
    private val operator: ComparisonOperator,
    private val rightValue: IValueNode<T>?
) : IEvaluatorNode {

    override fun evaluate(now: Calendar): Boolean {

        var leftValue: T? = null
        var rightValue: T? = null
        if (this.leftValue != null) {
            leftValue = this.leftValue.calculate(now)
        }

        if (this.rightValue != null) {
            rightValue = this.rightValue.calculate(now)
        }

        if (leftValue != null || rightValue != null) {
            when (operator) {
                ComparisonOperator.Greater -> return leftValue!!.asInteger() > rightValue!!.asInteger()
                ComparisonOperator.Lesser -> return leftValue!!.asInteger() < rightValue!!.asInteger()
                ComparisonOperator.Equals -> return leftValue!!.asInteger() == rightValue!!.asInteger()
                ComparisonOperator.NotEquals -> return leftValue!!.asInteger() != rightValue!!.asInteger()
                ComparisonOperator.GreaterOrEqual -> return leftValue!!.asInteger() >= rightValue!!.asInteger()
                ComparisonOperator.LesserOrEqual -> return leftValue!!.asInteger() <= rightValue!!.asInteger()
            }
        }

        return false
    }
}