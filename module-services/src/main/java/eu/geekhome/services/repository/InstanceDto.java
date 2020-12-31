package eu.geekhome.services.repository;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class InstanceDto {

    @SerializedName("id")
    private final long _id;

    @SerializedName("iconId")
    private final Long _iconId;

    @SerializedName("class")
    private final String _clazz;

    @SerializedName("fields")
    private final Map<String, String> _fields;

    public InstanceDto(long id, Long iconId, String clazz, Map<String, String> fields) {
        _id = id;
        _iconId = iconId;
        _clazz = clazz;
        _fields = fields;
    }

    public String getClazz() {
        return _clazz;
    }

    public Map<String, String> getFields() {
        return _fields;
    }

    public long getId() {
        return _id;
    }

    public Long getIconId() {
        return _iconId;
    }
}
