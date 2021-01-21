package com.geekhome.common;

import java.util.ArrayList;

public class ConfigurationMetadata {
    private boolean _current;
    private String _comment;
    private String _from;
    private String _version;
    private int _maxPool;
    private final ArrayList<SectionMetadata> _sections;

    public ConfigurationMetadata(String version, int maxPool, String comment, String from) {
        _version = version;
        _maxPool = maxPool;
        _comment = comment;
        _from = from;
        _sections = new ArrayList<>();
    }

    public ConfigurationMetadata() {
        this("1.0", 0, null, null);
    }

    public String getVersion() {
        return _version;
    }

    public void setVersion(String version) {
        _version = version;
    }

    public int getMaxPool() {
        return _maxPool;
    }

    public void setMaxPool(int maxPool) {
        _maxPool = maxPool;
    }

    public ArrayList<SectionMetadata> getSections() {
        return _sections;
    }

    public String getComment() {
        return _comment;
    }

    public void setComment(String comment) {
        _comment = comment;
    }

    public String getFrom() {
        return _from;
    }

    public void setFrom(String from) {
        _from = from;
    }

    public boolean isCurrent() {
        return _current;
    }

    public void setCurrent(boolean current) {
        _current = current;
    }
}
