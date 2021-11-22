package eu.automateeverything.timeplugin

import eu.automateeverything.domain.automation.ValueNode
import eu.automateeverything.data.hardware.PortValue
import java.util.*

class NowValueNode : ValueNode {
    override fun getValue(now: Calendar): PortValue {
        val nowSeconds = now.get(Calendar.HOUR_OF_DAY)*3600 + now.get(Calendar.MINUTE)*60 + now.get(Calendar.SECOND)
        return SecondOfDayStamp(nowSeconds.toDouble())
    }
}