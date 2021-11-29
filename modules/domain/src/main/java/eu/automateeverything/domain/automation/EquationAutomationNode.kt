package eu.automateeverything.domain.automation

import eu.automateeverything.domain.automation.blocks.MathOperator
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.domain.hardware.PortValueBuilder
import java.math.BigDecimal
import java.util.*

class EquationAutomationNode(
    private val valueType: Class<out PortValue>,
    private val leftValue: ValueNode?,
    private val operator: MathOperator,
    private val rightValue: BigDecimal
) : ValueNode {

    override fun getValue(now: Calendar): PortValue? {

        var leftValue: PortValue? = null
        if (this.leftValue != null) {
            leftValue = this.leftValue.getValue(now)
        }

        if (leftValue != null) {
            return when (operator) {
                MathOperator.Plus -> buildValue(leftValue.asDecimal().add(rightValue))
                MathOperator.Minus -> buildValue(leftValue.asDecimal().subtract(rightValue))
                MathOperator.Times -> buildValue(leftValue.asDecimal().times(rightValue))
                MathOperator.Divide -> buildValue(leftValue.asDecimal().divide(rightValue))
            }
        }

        return null
    }

    private fun buildValue(value: BigDecimal) : PortValue {
        return PortValueBuilder.buildFromDecimal(valueType, value)
    }
}