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

package eu.automateeverything.rest.inbox

import eu.automateeverything.R
import eu.automateeverything.data.inbox.InboxItemDto
import eu.automateeverything.data.inbox.InboxItemKind
import eu.automateeverything.data.inbox.InboxMessageDto
import eu.automateeverything.data.localization.Resource

class InboxMessageDtoMapper {
    fun map(from: InboxItemDto): InboxMessageDto {
        return InboxMessageDto(
            from.id,
            buildMessageSubject(from),
            buildMessageBody(from),
            from.timestamp,
            from.read
        )
    }

    private fun buildMessageSubject(from: InboxItemDto): Resource {
        return when (from.kind) {
            InboxItemKind.CustomMessage -> R.inbox_custom_message_subject
            InboxItemKind.WelcomeMessage -> R.inbox_message_welcome_subject
            InboxItemKind.NewPortFound -> R.inbox_message_new_port_found_subject
            InboxItemKind.AutomationEnabled -> R.inbox_message_automation_enabled_subject
            InboxItemKind.AutomationDisabled -> R.inbox_message_automation_disabled_subject
        }
    }

    private fun buildMessageBody(from: InboxItemDto): Resource {
        return when (from.kind) {
            InboxItemKind.CustomMessage -> return if (from.message == null) {
                R.inbox_custom_message_empty
            } else {
                Resource.createUniResource(from.message!!)
            }
            InboxItemKind.WelcomeMessage -> R.inbox_message_welcome_description_body
            InboxItemKind.NewPortFound -> R.inbox_message_port_found_body(from.newPortId)
            InboxItemKind.AutomationEnabled -> R.inbox_message_automation_enabled_body
            InboxItemKind.AutomationDisabled -> R.inbox_message_automation_disabled_body
        }
    }
}