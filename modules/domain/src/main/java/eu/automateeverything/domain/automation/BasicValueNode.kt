package eu.automateeverything.domain.automation

import eu.automateeverything.domain.hardware.PortValue
import java.util.*

class BasicValueNode<T: PortValue>(val value: T) : ValueNode {
    override fun getValue(now: Calendar): T {
        return value
    }
}