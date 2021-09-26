package eu.automateeverything.domain.inbox

import eu.automateeverything.data.Repository
import eu.automateeverything.data.inbox.InboxItemDto
import eu.automateeverything.data.inbox.InboxItemKind
import eu.automateeverything.domain.events.EventsSink
import java.util.*

class BroadcastingInbox(
    val eventsSink: EventsSink,
    val repository: Repository
) : Inbox {

    init {
        refreshUnreadMessages()
    }

    override fun sendCustomMessage(message: String) {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.CustomMessage,
            message = message
        )

        inboxItem.id = repository.saveInboxItem(inboxItem)
        eventsSink.broadcastInboxMessage(inboxItem)
        unreadMessagesCount++
    }

    override fun sendAppStarted() {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.WelcomeMessage
        )

        inboxItem.id = repository.saveInboxItem(inboxItem)
        eventsSink.broadcastInboxMessage(inboxItem)
        unreadMessagesCount++
    }

    override fun sendNewPortDiscovered(newPortId: String) {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.NewPortFound,
            newPortId = newPortId
        )

        inboxItem.id = repository.saveInboxItem(inboxItem)
        eventsSink.broadcastInboxMessage(inboxItem)
        unreadMessagesCount++
    }

    override fun sendAutomationStarted() {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.AutomationEnabled
        )

        inboxItem.id = repository.saveInboxItem(inboxItem)
        eventsSink.broadcastInboxMessage(inboxItem)
        unreadMessagesCount++
    }

    override fun sendAutomationStopped() {
        val inboxItem = InboxItemDto(
            timestamp = calculateNow(),
            kind = InboxItemKind.AutomationDisabled
        )

        inboxItem.id = repository.saveInboxItem(inboxItem)
        eventsSink.broadcastInboxMessage(inboxItem)
        unreadMessagesCount++
    }

    override var unreadMessagesCount : Int = 0

    override fun refreshUnreadMessages() {
        unreadMessagesCount = repository.getUnreadInboxItems().size
    }

    private fun calculateNow(): Long {
        return Calendar.getInstance().timeInMillis
    }
}