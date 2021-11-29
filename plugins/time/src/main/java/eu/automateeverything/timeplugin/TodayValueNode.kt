package eu.automateeverything.timeplugin

import eu.automateeverything.domain.automation.ValueNode
import eu.automateeverything.data.hardware.PortValue
import java.util.*

class TodayValueNode : ValueNode {
    override fun getValue(now: Calendar): PortValue {
        val calCopy = Calendar.getInstance()
        calCopy.set(0, now.get(Calendar.MONTH), now.get(Calendar.DATE))
        val dayOfYear = calCopy.get(Calendar.DAY_OF_YEAR)
        return DayOfYearStamp(dayOfYear.toBigDecimal())
    }
}