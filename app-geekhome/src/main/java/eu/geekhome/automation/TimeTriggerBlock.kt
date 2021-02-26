package eu.geekhome.automation

import java.util.*

class TimeTriggerBlock(
    private val seconds: Int,
    override val next: StatementNode?
    ) : StatementNode {

    private var lastProcessed: Long = 0

    override fun process(now: Calendar) {
        val timeInMillis = now.timeInMillis

        if (lastProcessed + seconds * 1000 > timeInMillis) {
            lastProcessed = timeInMillis

            next?.process(now)
        }
    }
}