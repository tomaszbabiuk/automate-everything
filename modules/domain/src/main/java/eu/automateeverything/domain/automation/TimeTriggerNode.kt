package eu.automateeverything.domain.automation

import java.util.*

class TimeTriggerNode(
    private val seconds: Int,
    override val next: IStatementNode?
    ) : StatementNodeBase() {

    private var lastProcessed: Long = 0

    override fun process(now: Calendar, firstLoop: Boolean) {
        val timeInMillis = now.timeInMillis

        if (lastProcessed + seconds * 1000 < timeInMillis) {
            lastProcessed = timeInMillis

            next?.process(now, firstLoop)
        }
    }
}