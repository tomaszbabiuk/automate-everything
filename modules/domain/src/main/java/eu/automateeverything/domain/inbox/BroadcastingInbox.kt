/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
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

import eu.automateeverything.data.DataRepository
import eu.automateeverything.data.inbox.InboxItemDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.events.EventBus
import java.util.*

class BroadcastingInbox(val eventBus: EventBus, val dataRepository: DataRepository) : Inbox {

    init {
        refreshCounters()
    }

    override fun sendMessage(subject: Resource, body: Resource) {
        val inboxItem =
            InboxItemDto(
                timestamp = calculateNow(),
                subject = subject.serialize(),
                body = body.serialize()
            )

        inboxItem.id = dataRepository.saveInboxItem(inboxItem)
        eventBus.broadcastInboxMessage(inboxItem)
        refreshCounters()
    }

    override var unreadMessagesCount: Long = 0
    override var totalMessagesCount: Long = 0

    override fun refreshCounters() {
        unreadMessagesCount = dataRepository.countUnreadInboxItems()
        totalMessagesCount = dataRepository.countAllInboxItems()
    }

    private fun calculateNow(): Long {
        return Calendar.getInstance().timeInMillis
    }
}
