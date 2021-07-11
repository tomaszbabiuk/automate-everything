package eu.geekhome.domain.inbox

import eu.geekhome.data.Repository
import eu.geekhome.data.inbox.InboxItemDto
import eu.geekhome.data.inbox.InboxItemKind
import eu.geekhome.domain.events.EventsSink
import java.util.*

class Inbox(
    val eventsSink: EventsSink,
    val repository: Repository
) {

    fun broadcastCustomMessage(message: String) {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.CustomMessage,
            message = message)

        eventsSink.broadcastInboxMessage(inboxItem)
        repository.saveInboxItem(inboxItem)
    }

    fun broadcastAppStarted() {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.WelcomeMessage)

        eventsSink.broadcastInboxMessage(inboxItem)
        repository.saveInboxItem(inboxItem)
    }

    fun broadcastNewPortDiscovered(newPortId: String) {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.NewPortFound,
            newPortId = newPortId)

        eventsSink.broadcastInboxMessage(inboxItem)
        repository.saveInboxItem(inboxItem)
    }

    fun broadcastAutomationStarted() {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.AutomationStarted
        )

        eventsSink.broadcastInboxMessage(inboxItem)
        repository.saveInboxItem(inboxItem)
    }

    fun broadcastAutomationStopped() {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.AutomationStopped
        )

        eventsSink.broadcastInboxMessage(inboxItem)
        repository.saveInboxItem(inboxItem)
    }

    private fun calculateNow(): Long {
        return Calendar.getInstance().timeInMillis
    }
}
