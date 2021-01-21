package com.geekhome.common.commands;

import com.geekhome.common.localization.ILocalizationProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FailedMessage extends AwaitingMessage {
    private final Date _failedAt;

    @Override
    public String toString() {
        return String.format("Message to: '%s', subject: '%s'. Failed to sent at: '%s'", getTargetUser(), getSubject(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_failedAt));
    }

    public FailedMessage(AwaitingMessage email, ILocalizationProvider localizationProvider) {
        super(email.getTargetUser(), email.getSubject(), null);
        _failedAt = new Date();
        setContent(String.format(localizationProvider.getValue("C:MessageMayBeExpired"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_failedAt)) + "\r\n" + email.getContent());
    }
}
