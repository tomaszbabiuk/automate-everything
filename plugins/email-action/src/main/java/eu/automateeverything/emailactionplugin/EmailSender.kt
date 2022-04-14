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

package eu.automateeverything.emailactionplugin

import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.net.ssl.SSLSocketFactory

object EmailSender {

    @Throws(MessagingException::class)
    fun sendEmailInternal(host: String, username: String, password: String, targetUser: String, subject: String, content: String) {
        val props = System.getProperties()
        props.setProperty("mail.store.protocol", "imaps")
        props["mail.smtp.host"] = host
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = SSLSocketFactory::class.java.name
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
        props["mail.imap.connectiontimeout"] = "30000"
        props["mail.imap.timeout"] = "30000"
        val session = Session.getDefaultInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        val message: Message = composeEmailMessage(session, targetUser, subject, content)
        Transport.send(message)
    }

    @Throws(MessagingException::class)
    private fun composeEmailMessage(session: Session, to: String, subject: String, content: String): Message {
        val message: Message = MimeMessage(session)
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
        message.subject = subject
        message.setText(content)
        return message
    }
}