package eu.automateeverything.domain.automation

import eu.automateeverything.domain.automation.blocks.MathOperator
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.domain.hardware.PortValueBuilder
import java.util.*

class EquationAutomationNode(
    private val valueType: Class<out PortValue>,
    private val leftValue: ValueNode?,
    private val operator: MathOperator,
    private val rightValue: Double
) : ValueNode {

    override fun getValue(now: Calendar): PortValue? {

        var leftValue: PortValue? = null
        if (this.leftValue != null) {
            leftValue = this.leftValue.getValue(now)
        }

        if (leftValue != null) {
            return when (operator) {
                MathOperator.Plus -> buildValue(leftValue.asDouble() + rightValue)
                MathOperator.Minus -> buildValue(leftValue.asDouble() - rightValue)
                MathOperator.Times -> buildValue(leftValue.asDouble() * rightValue)
                MathOperator.Divide -> buildValue(leftValue.asDouble() / rightValue)
            }
        }

        return null
    }

    private fun buildValue(value: Double) : PortValue {
        return PortValueBuilder.buildFromDouble(valueType, value)
    }
}