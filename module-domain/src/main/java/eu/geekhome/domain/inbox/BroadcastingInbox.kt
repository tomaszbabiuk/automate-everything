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

        inboxItem.id = repository.saveInboxItem(inboxItem)
        eventsSink.broadcastInboxMessage(inboxItem)
    }

    override fun sendAppStarted() {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.WelcomeMessage
        )

        inboxItem.id = repository.saveInboxItem(inboxItem)
        eventsSink.broadcastInboxMessage(inboxItem)
    }

    override fun sendNewPortDiscovered(newPortId: String) {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.NewPortFound,
            newPortId = newPortId
        )

        inboxItem.id = repository.saveInboxItem(inboxItem)
        eventsSink.broadcastInboxMessage(inboxItem)
    }

    override fun sendAutomationStarted() {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.AutomationEnabled
        )

        inboxItem.id = repository.saveInboxItem(inboxItem)
        eventsSink.broadcastInboxMessage(inboxItem)
    }

    override fun sendAutomationStopped() {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.AutomationDisabled
        )

        inboxItem.id = repository.saveInboxItem(inboxItem)
        eventsSink.broadcastInboxMessage(inboxItem)
    }

    private fun calculateNow(): Long {
        return Calendar.getInstance().timeInMillis
    }
}