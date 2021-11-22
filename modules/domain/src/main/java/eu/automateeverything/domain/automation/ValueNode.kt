package eu.automateeverything.domain.automation

import eu.automateeverything.data.hardware.PortValue
import java.util.*

interface ValueNode: AutomationNode {
    fun getValue(now: Calendar) : PortValue?
}