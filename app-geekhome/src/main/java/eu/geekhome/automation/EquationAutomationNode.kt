package eu.geekhome.automation

import eu.geekhome.automation.blocks.MathOperator
import eu.geekhome.services.hardware.PortValue
import eu.geekhome.services.hardware.Temperature
import java.util.*

abstract class EquationAutomationNode<T: PortValue>(
    private val leftValue: IValueNode<T>?,
    private val operator: MathOperator,
    private val rightValue: Double
) : IValueNode<T> {

    override fun calculate(now: Calendar): T? {

        var leftValue: T? = null
        if (this.leftValue != null) {
            leftValue = this.leftValue.calculate(now)
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

    abstract fun buildValue(value: Double) : T
}

class TemperatureEquationAutomationNode(
    leftValue: IValueNode<Temperature>?,
    operator: MathOperator,
    rightValue: Double
) : EquationAutomationNode<Temperature>(leftValue, operator, rightValue) {

    override fun buildValue(value: Double): Temperature {
        return Temperature(value)
    }
}