package eu.geekhome.domain.inbox

import eu.geekhome.data.Repository
import eu.geekhome.data.inbox.InboxItemDto
import eu.geekhome.data.inbox.InboxItemKind
import eu.geekhome.domain.events.EventsSink
import java.util.*

class BroadcastingInbox(
    val eventsSink: EventsSink,
    val repository: Repository
) : Inbox {

    override fun sendCustomMessage(message: String) {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.CustomMessage,
            message = message
        )

        eventsSink.broadcastInboxMessage(inboxItem)
        repository.saveInboxItem(inboxItem)
    }

    override fun sendAppStarted() {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.WelcomeMessage
        )

        eventsSink.broadcastInboxMessage(inboxItem)
        repository.saveInboxItem(inboxItem)
    }

    override fun sendNewPortDiscovered(newPortId: String) {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.NewPortFound,
            newPortId = newPortId
        )

        eventsSink.broadcastInboxMessage(inboxItem)
        repository.saveInboxItem(inboxItem)
    }

    override fun sendAutomationStarted() {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.AutomationStarted
        )

        eventsSink.broadcastInboxMessage(inboxItem)
        repository.saveInboxItem(inboxItem)
    }

    override fun sendAutomationStopped() {
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