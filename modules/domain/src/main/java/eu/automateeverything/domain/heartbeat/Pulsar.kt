package eu.automateeverything.domain.heartbeat

import eu.automateeverything.domain.WithStartStopScope
import eu.automateeverything.domain.automation.AutomationConductor
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.inbox.Inbox
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*

class Pulsar(
    val eventsSink: EventsSink,
    val inbox: Inbox,
    val automationConductor: AutomationConductor
    ) : WithStartStopScope() {
    override fun start() {
        super.start()
        startStopScope.launch {
            while (isActive) {
                sendHeartbeatEvent()
                delay(10000)
            }
        }
    }

    private fun sendHeartbeatEvent() {
        val timestamp = Calendar.getInstance().timeInMillis
        val unreadMessagesCount = inbox.unreadMessagesCount
        val isAutomationEnabled = automationConductor.isEnabled()

        eventsSink.broadcastHeartbeatEvent(timestamp, unreadMessagesCount, isAutomationEnabled)
    }
}