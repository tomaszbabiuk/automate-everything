package eu.geekhome.services.repository;

import com.google.gson.annotations.SerializedName;

public class FieldInstanceDto {

    @SerializedName("id")
    private long _id;

    @SerializedName("name")
    private String _name;

    @SerializedName("value")
    private String _valueAsString;

    @SerializedName("instanceId")
    private long _instanceId;

    public FieldInstanceDto(long id, String name, String valueAsString, long instanceId) {
        _id = id;
        _name = name;
        _valueAsString = valueAsString;
        _instanceId = instanceId;
    }
}
