package com.geekhome.common.configuration;

import com.geekhome.common.PersistableReflector;
import com.google.gson.annotations.SerializedName;

public class DescriptiveName {

    @SerializedName("name")
    private String _name;

    @SerializedName("description")
    private String _description;

    @SerializedName("uniqueId")
    private String _uniqueId;

    public DescriptiveName(String name, String uid, String description) {
        this._name = name;
        this._uniqueId = uid;
        this._description = description;
    }

    public DescriptiveName(String name, String uid, boolean descriptionEqualsName) {
        this(name, uid, descriptionEqualsName ? name : "");
    }

    public DescriptiveName(String name, String uid) {
        this(name, uid, "");
    }

    @Persistable(name = "Name")
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Persistable(name = "Description")
    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    @Persistable(name = "UniqueId")
    public String getUniqueId() {
        return _uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this._uniqueId = uniqueId;
    }

    @Override
    public String toString() {
        if (getDescription() != null && !getDescription().equals("")) {
            return String.format("%s (%s)", getName(), getDescription());
        }

        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        return ((DescriptiveName)obj).getUniqueId().equals(getUniqueId());
    }
}
