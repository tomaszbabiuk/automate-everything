package com.geekhome.common.commands;


public class AwaitingMessage {
    private String _targetUser;
    private String _subject;
    private String _content;
    private String _contentType;
    private String _json;
    private long _id;

    public String getTargetUser() {
        return _targetUser;
    }

    public String getSubject() {
        return _subject;
    }

    public String getContent() {
        return _content;
    }

    public void setContent(String value) {
        _content = value;
    }

    @Override
    public String toString() {
        return String.format("Email to: '%s', subject: '%s'.", _targetUser, _subject);
    }

    public AwaitingMessage(String targetUser, String subject, String content) {
        _targetUser = targetUser;
        _subject = subject;
        _content = content;
    }

    public String getContentType() {
        return _contentType;
    }

    public String getJson() {
        return _json;
    }

    public void setJson(String json) {
        _json = json;
    }

    public long getId() {
        return _id;
    }

    public void setAdditionalParameters(long id, String contentType, String json) {
        _id = id;
        _contentType = contentType;
        _json = json;
    }
}