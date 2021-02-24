package eu.geekhome.automation

class TimeTriggerBlock(
    private val seconds: Int,
    override val next: AutomationNode?
    ) : AutomationNode {

    private var lastProcessed: Long = 0

    override fun process(timeInMillis: Long) {

        if (lastProcessed + seconds * 1000 > timeInMillis) {
            lastProcessed = timeInMillis
            next?.process(timeInMillis)
        }
    }
}