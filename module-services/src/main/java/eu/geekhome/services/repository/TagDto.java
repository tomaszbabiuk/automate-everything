package eu.geekhome.services.repository;

import com.google.gson.annotations.SerializedName;

public class TagDto {

    @SerializedName("id")
    private final long _id;

    @SerializedName("parentId")
    private final Long _parentId;

    @SerializedName("name")
    private final String _name;

    public TagDto(long id, Long parentId, String name) {
        _id = id;
        _parentId = parentId;
        _name = name;
    }

    public long getId() {
        return _id;
    }

    public Long getParentId() {
        return _parentId;
    }

    public String getName() {
        return _name;
    }
}
