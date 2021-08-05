package eu.geekhome.domain.automation

import eu.geekhome.data.localization.Resource
import java.util.*

class TimeTriggerNode(
    private val seconds: Int,
    override val next: IStatementNode?
    ) : IStatementNode {

    private var lastProcessed: Long = 0

    override fun process(now: Calendar, firstLoop: Boolean, notes: MutableList<Resource>) {
        val timeInMillis = now.timeInMillis

        if (lastProcessed + seconds * 1000 < timeInMillis) {
            lastProcessed = timeInMillis

            next?.process(now, firstLoop, notes)
        }
    }
}