package com.geekhome.synchronizationmodule;

import com.google.gson.annotations.SerializedName;

public class SynchronizedNamedObject {
    @SerializedName("name")
    private String _name;

    @SerializedName("description")
    private String _description;

    @SerializedName("uniqueId")
    private String _uniqueId;

    public SynchronizedNamedObject() {
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getUniqueId() {
        return _uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        _uniqueId = uniqueId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.equals(getUniqueId());
    }
}