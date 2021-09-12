package eu.geekhome.rest.inbox

import eu.geekhome.R
import eu.geekhome.data.inbox.InboxItemDto
import eu.geekhome.data.inbox.InboxItemKind
import eu.geekhome.data.inbox.InboxMessageDto
import eu.geekhome.data.localization.Resource

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