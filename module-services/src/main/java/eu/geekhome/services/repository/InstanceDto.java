package eu.geekhome.services.repository;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class InstanceDto {

    @SerializedName("id")
    private long _id;

    @SerializedName("class")
    private String _clazz;

    @SerializedName("fields")
    private Map<String, String> _fields;

    public InstanceDto(long id, String clazz, Map<String, String> fields) {
        _id = id;
        _clazz = clazz;
        _fields = fields;
    }
}
