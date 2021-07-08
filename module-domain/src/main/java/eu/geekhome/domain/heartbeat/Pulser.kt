package eu.geekhome.domain.heartbeat

import eu.geekhome.domain.WithStartStopScope
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.events.LiveEventsHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*

class Pulser(val eventsSink: EventsSink) : WithStartStopScope() {
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
        LiveEventsHelper.broadcastHeartbeatEvent(eventsSink, timestamp)
    }
}