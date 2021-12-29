/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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