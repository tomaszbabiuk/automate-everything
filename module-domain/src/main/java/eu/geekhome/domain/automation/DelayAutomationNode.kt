package eu.geekhome.domain.automation

import eu.geekhome.data.localization.Resource
import kotlinx.coroutines.*
import java.util.*

class DelayAutomationNode(
    override val next: IStatementNode?,
    private val doNode: IStatementNode?,
    private val seconds: Int) : IStatementNode {

    private var scope: CoroutineScope? = null

    override fun process(now: Calendar, firstLoop: Boolean, notes: MutableList<Resource>) {
        notes.add(Resource.createUniResource("dupa"))

        if (scope != null) {
            scope!!.cancel("Clearing old scope of delay automation node")
        }

        if (doNode != null) {
            scope = CoroutineScope(Dispatchers.IO)
            scope!!.launch {
                var secondsTillEnd = seconds
                while (isActive && secondsTillEnd >= 0) {
                    delay(1000)
                    secondsTillEnd--
                }

                if (isActive) {
                    doNode.process(Calendar.getInstance(), firstLoop, notes)
                }
            }
        }
    }

}
