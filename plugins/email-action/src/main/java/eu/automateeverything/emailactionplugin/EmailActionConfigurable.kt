package eu.automateeverything.emailactionplugin

import eu.automateeverything.actions.ActionConfigurableBase
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.StateChangeReporter
import org.pf4j.Extension
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.net.ssl.SSLSocketFactory


@Extension
class EmailActionConfigurable(stateChangeReporter: StateChangeReporter) :
    ActionConfigurableBase(stateChangeReporter) {

    override val titleRes: Resource
        get() = R.configurable_gmail_action_title

    override val descriptionRes: Resource
        get() = R.configurable_gmail_action_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg" xmlns:se="http://svg-edit.googlecode.com" data-name="Layer 1">
             <title>big4_outline</title>
             <g class="layer">
              <title>terminal by Hare Krishna from the Noun Project</title>
              <path d="m87.83,12.83l-75.66,0a8,8 0 0 0 -8,8l0,54.67a8,8 0 0 0 8,8l75.66,0a8,8 0 0 0 8,-8l0,-54.67a8,8 0 0 0 -8,-8zm4,62.67a4,4 0 0 1 -4,4l-75.66,0a4,4 0 0 1 -4,-4l0,-54.67a4,4 0 0 1 4,-4l75.66,0a4,4 0 0 1 4,4l0,54.67zm-60,-40.33a2,2 0 0 1 0,2.83l-8.83,8.83a2,2 0 0 1 -2.83,-2.83l7.42,-7.42l-7.5,-7.5a2,2 0 0 1 2.83,-2.83l8.91,8.92zm21.17,10.25a2,2 0 0 1 -2,2l-14.5,0a2,2 0 0 1 0,-4l14.5,0a2,2 0 0 1 2,2z" id="svg_1"/>
             </g>
            </svg>
        """.trimIndent()

    override fun executionCode(instance: InstanceDto): Pair<Boolean,Resource> {
        sendEmailInternal("tomasz.babiuk@gmail.com", "subject", "content")
        return Pair(true, Resource.createUniResource("ok"))
    }

    override val addNewRes = R.configurable_gmail_action_add

    override val editRes = R.configurable_gmail_action_edit

    companion object {
        const val FIELD_COMMAND = "command"
    }

    @Throws(MessagingException::class)
    private fun sendEmailInternal(targetUser: String, subject: String, content: String) {
        val session = startSession()!!
        val message: Message = composeEmailMessage(session, targetUser, subject, content)
        Transport.send(message)
    }

    @Throws(MessagingException::class)
    private fun composeEmailMessage(session: Session, to: String, subject: String, content: String): Message {
        val message: Message = MimeMessage(session)
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
        message.setSubject(subject)
        message.setText(content)
        return message
    }

    private fun startSession(): Session? {
        val props = System.getProperties()
        val settingsSmtpHost = "smtp.gmail.com"
        val settingsUser = "tomasz.babiuk@gmail.com"
        val settingsPassword = "C@l1nk@wg2"
        props.setProperty("mail.store.protocol", "imaps")
        props["mail.smtp.host"] = settingsSmtpHost
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = SSLSocketFactory::class.java.name
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
        props["mail.imap.connectiontimeout"] = "30000"
        props["mail.imap.timeout"] = "30000"
            return Session.getDefaultInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(settingsUser, settingsPassword)
                }
            })
    }
}